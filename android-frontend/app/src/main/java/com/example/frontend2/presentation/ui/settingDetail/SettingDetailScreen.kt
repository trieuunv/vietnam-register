package com.example.frontend2.presentation.ui.settingDetail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PinEnd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    navController: NavController,
    viewModel: SettingDetailViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val hasScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset > 0
        }
    }
    val appBarElevation by animateDpAsState(targetValue = if (hasScrolled) 4.dp else 0.dp)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(appBarElevation),
                title = { Text(text = "Cài đặt") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = { },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(vertical = 12.dp),
                state = listState
            ) {
                item {
                    GroupContainer {
                        CategoryItem(
                            "Hồ sơ cá nhân", Icons.Filled.AccountCircle, Color(0xFF3097FA)
                        ) { navController.navigate(Screen.ProfileDetail.route) }
                        CategoryItem(
                            "Quyền riêng tư", Icons.Filled.Lock, Color(0xFFF8BF3F)
                        ) { /* TODO */ }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    GroupContainer {
                        CategoryItem(
                            "Danh sách cuộc hẹn", Icons.Filled.EditNote, Color(0xFF70C941)
                        ) { navController.navigate(Screen.AppointmentManager.route) }
                        CategoryItem(
                            "Hồ sơ phương tiện", Icons.Filled.DirectionsCar, Color(0xFFFD6572)
                        ) { navController.navigate(Screen.VehicleManager.route) }
                        CategoryItem(
                            "Thông báo", Icons.Filled.Notifications, Color(0xFF3099FC)
                        ) { /* TODO */ }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    GroupContainer {
                        CategoryItem(
                            "Góp ý, báo cáo sai phạm", Icons.Filled.AutoAwesome, Color(0xFF71C53E)
                        ) { /* TODO */ }
                        CategoryItem(
                            "Điều khoản & chính sách", Icons.Filled.Description, Color(0xFF3EA3F8)
                        ) { /* TODO */ }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    GroupContainer {
                        CategoryItem("Đăng xuất", Icons.Filled.PinEnd, Color(0xFF7D98B6)) {
                            viewModel.logout { navController.navigate("login") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupContainer(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CategoryItem(title: String, icon: ImageVector, iconColor: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = iconColor
            )
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}