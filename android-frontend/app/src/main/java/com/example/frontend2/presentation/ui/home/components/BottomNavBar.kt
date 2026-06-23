package com.example.frontend2.presentation.ui.home.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable


data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navigationBarItems = listOf(
        BottomNavItem("Trang chủ", Icons.Default.Home, "main"),
        BottomNavItem("Tiện ích", Icons.Default.EditNote, "extension"),
        BottomNavItem("Thông báo", Icons.Default.NotificationsActive, "notification"),
        BottomNavItem("Tài khoản", Icons.Default.Person, "profile")
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val selectedIndex = navigationBarItems.indexOfFirst { it.route == currentRoute }
        .takeIf { it >= 0 } ?: 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedNavigationBar(
            modifier = Modifier.height(54.dp),
            selectedIndex = selectedIndex,
            cornerRadius = shapeCornerRadius(28.dp),
            barColor = MaterialTheme.colorScheme.surface,
            ballColor = MaterialTheme.colorScheme.inversePrimary,
            ballAnimation = Teleport(tween(800)),
            indentAnimation = Height(tween(300)),
        ) {
            navigationBarItems.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            if (selectedIndex != index) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selectedIndex == index)
                                MaterialTheme.colorScheme.inversePrimary
                            else
                                MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selectedIndex == index)
                                MaterialTheme.colorScheme.inversePrimary
                            else
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}