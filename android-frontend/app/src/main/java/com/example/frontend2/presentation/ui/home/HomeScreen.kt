package com.example.frontend2.presentation.ui.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontend2.presentation.ui.home.components.BottomNavBar
import com.example.frontend2.presentation.ui.home.extension.ExtensionScreen
import com.example.frontend2.presentation.ui.home.main.MainScreen
import com.example.frontend2.presentation.ui.home.notification.NotificationScreen
import com.example.frontend2.presentation.ui.home.profile.ProfileScreen

@Composable
fun HomeScreen(
    navController: NavController,
    homeNavController: NavHostController
) {
    Scaffold(
        bottomBar = { BottomNavBar(homeNavController) }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = "main"
        ) {
            composable("main") { MainScreen(navController) }
            composable("extension") { ExtensionScreen(navController) }
            composable("notification") { NotificationScreen(navController) }
            composable("profile") { ProfileScreen(navController, innerPadding = innerPadding) }
        }
    }
}
