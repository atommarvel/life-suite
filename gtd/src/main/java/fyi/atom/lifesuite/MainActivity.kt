package fyi.atom.lifesuite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fyi.atom.lifesuite.ui.theme.LifeSuiteTheme
import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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