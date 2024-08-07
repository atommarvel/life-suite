package fyi.atom.lifesuite.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute() {
    val vm: HomeViewModel = hiltViewModel()
    val hod by vm.state.collectAsState(HomeScreenHod.Loading)
    val sideEffect by vm.sideEffects.collectAsState(null)
    OnSideEffect(sideEffect)
    HomeScreen(hod, vm::dispatch)
}

@Composable
@ReadOnlyComposable
private fun OnSideEffect(sideEffect: HomeScreenSideEffect?) {
    if (sideEffect != null) {
        val context = LocalContext.current
        when (sideEffect) {
            is HomeScreenSideEffect.Snackbar ->
                Toast.makeText(context, sideEffect.msgRes, Toast.LENGTH_SHORT).show()
        }
    }
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
        when (hod) {
            HomeScreenHod.Loading -> CircularProgressIndicator()
            HomeScreenHod.LoginRequired ->
                Button(onClick = { dispatch(HomeScreenAction.OnLoginClick) }) {
                    Text(text = "Login to ClickUp")
                }
            is HomeScreenHod.Tasks ->
                Column {
                    hod.titles.forEach {
                        Text(it)
                    }
                }
            HomeScreenHod.LoggingIn -> Text(text = "Logging in...")
        }
    }
}
