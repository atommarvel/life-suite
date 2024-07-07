package fyi.atom.lifesuite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fyi.atom.lifesuite.auth.LogIntoClickUpLauncher
import fyi.atom.lifesuite.home.HomeRoute
import fyi.atom.lifesuite.ui.theme.LifeSuiteTheme
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
object HomeDestination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var logIntoClickUpLauncher: LogIntoClickUpLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeSuiteTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HomeDestination) {
                    composable<HomeDestination> { HomeRoute() }
                }
            }
        }
    }
}
