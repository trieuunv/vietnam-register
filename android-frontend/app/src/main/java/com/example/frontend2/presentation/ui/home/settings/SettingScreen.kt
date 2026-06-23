package com.example.frontend2.presentation.ui.home.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.presentation.navigation.Screen

@Composable
fun SettingScreen(navController: NavController, viewModel: SettingViewModel = hiltViewModel()) {

    Column {
        Text("Settings")

        Button(onClick = { navController.navigate(Screen.SettingDetail.route) }) {
            Text("Setting Detail")
        }

        Button(onClick = {
            viewModel.logout { navController.navigate(Screen.Login.route) }
        }) {
            Text("Logout")
        }
    }
}