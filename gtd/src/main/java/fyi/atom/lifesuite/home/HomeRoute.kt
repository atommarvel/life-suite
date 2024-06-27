package fyi.atom.lifesuite.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute() {
    val vm: HomeViewModel = hiltViewModel()
    val hod by vm.state.collectAsState(HomeScreenHod.Loading)
    HomeScreen(hod, vm::dispatch)
}

@Composable
fun HomeScreen(
    hod: HomeScreenHod,
    dispatch: (HomeScreenAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(hod) {
            HomeScreenHod.Loading -> CircularProgressIndicator()
            is HomeScreenHod.LoginCompleted -> Text("Login complete!")
            HomeScreenHod.LoginRequired -> Button(onClick = { dispatch(HomeScreenAction.OnLoginClick) }) {
                Text(text = "Login to ClickUp")
            }
        }
    }
}