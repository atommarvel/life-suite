package fyi.atom.lifesuite.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.flowredux.dsl.ChangedState
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.freeletics.flowredux.dsl.State
import dagger.hilt.android.lifecycle.HiltViewModel
import fyi.atom.lifesuite.api.clickup.FetchTasksInViewUseCase
import fyi.atom.lifesuite.auth.AuthState
import fyi.atom.lifesuite.auth.ClickUpAuthRepository
import fyi.atom.lifesuite.dev.logEach
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clickUpAuthRepo: ClickUpAuthRepository,
    private val fetchTasksInViewUseCase: FetchTasksInViewUseCase,
) : ViewModel() {
    private val stateMachine = HomeStateMachine()
    val state: Flow<HomeScreenHod> = stateMachine.state.logEach("HomeViewModel.state")

    fun dispatch(action: HomeScreenAction) {
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    inner class HomeStateMachine : FlowReduxStateMachine<HomeScreenHod, HomeScreenAction>(HomeScreenHod.Loading) {
        init {
            spec {
                inState<HomeScreenHod.Loading> {
                    collectWhileInState(clickUpAuthRepo.authState, handler = ::loadTasks)
                }

                inState<HomeScreenHod.LoginRequired> {
                    collectWhileInState(clickUpAuthRepo.authState) { authState: AuthState?, state ->
                        if (authState != null) state.override { HomeScreenHod.Loading } else state.noChange()
                    }
                    onActionEffect<HomeScreenAction.OnLoginClick> { _, _ ->
                        clickUpAuthRepo.launchLoginRequest()
                    }
                }
            }
        }

        private suspend fun loadTasks(
            authState: AuthState?,
            state: State<HomeScreenHod.Loading>
        ): ChangedState<HomeScreenHod> =
            if (authState != null) {
                val titles = fetchTasksInViewUseCase().tasks?.map { it.name }.orEmpty()
                state.override { HomeScreenHod.Tasks(titles) }
            } else {
                state.override { HomeScreenHod.LoginRequired }
            }
    }
}

sealed interface HomeScreenHod {
    data object Loading : HomeScreenHod
    data object LoginRequired : HomeScreenHod
    data class Tasks(val titles: List<String>) : HomeScreenHod
}

sealed interface HomeScreenAction {
    data object OnLoginClick : HomeScreenAction
}