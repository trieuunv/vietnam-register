package com.example.frontend2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend2.presentation.navigation.NavGraph
import com.example.frontend2.presentation.navigation.Screen
import com.example.frontend2.presentation.theme.Frontend2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Frontend2Theme {
                MainNavigation(mainViewModel)
            }
        }
    }
}

@Composable
fun MainNavigation(viewModel: MainViewModel) {
    val navController: NavHostController = rememberNavController()
    val isLoggedIn = viewModel.isLoggedIn.value

    if (isLoggedIn) {
        NavGraph(navController = rememberNavController(), Screen.Home.route)
    } else {
        NavGraph(navController, Screen.Login.route)
    }
}


