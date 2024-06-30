package fyi.atom.lifesuite.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.freeletics.flowredux.dsl.State
import dagger.hilt.android.lifecycle.HiltViewModel
import fyi.atom.lifesuite.api.FetchTasksInViewUseCase
import fyi.atom.lifesuite.auth.AuthState
import fyi.atom.lifesuite.auth.ClickUpAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // TODO: instead just have a repo to get the clickup data
    private val clickUpAuthRepo: ClickUpAuthRepository,
    private val fetchTasksInViewUseCase: FetchTasksInViewUseCase,
) : ViewModel() {
    private val stateMachine = HomeStateMachine()
    val state: Flow<HomeScreenHod> = stateMachine.state

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
                    collectWhileInState(clickUpAuthRepo.authState) { authState: AuthState?, state: State<HomeScreenHod.Loading> ->
                        if (authState == null) {
                            state.override { HomeScreenHod.LoginRequired }
                        } else {
                            state.override { HomeScreenHod.LoginCompleted(authState) }
                        }
                    }
                }

                inState<HomeScreenHod.LoginRequired> {
                    collectWhileInState(clickUpAuthRepo.authState) { authState: AuthState?, state: State<HomeScreenHod.LoginRequired> ->
                        if (authState != null) {
                            state.override { HomeScreenHod.LoginCompleted(authState) }
                        } else {
                            state.noChange()
                        }
                    }
                    onActionEffect<HomeScreenAction.OnLoginClick> { _, _ ->
                        clickUpAuthRepo.launchLoginRequest()
                    }
                }

                inState<HomeScreenHod.LoginCompleted> {
                    onEnter { state ->
                        val titles = fetchTasksInViewUseCase.invoke("a45kv-514").tasks?.map { it.name }.orEmpty()
                        state.override { HomeScreenHod.Tasks(titles) }
                    }
                }
            }
        }
    }
}

sealed interface HomeScreenHod {
    data object Loading : HomeScreenHod
    data object LoginRequired : HomeScreenHod
    data class LoginCompleted(val authState: AuthState) : HomeScreenHod
    data class Tasks(val titles: List<String>): HomeScreenHod
}

sealed interface HomeScreenAction {
    data object OnLoginClick : HomeScreenAction
}