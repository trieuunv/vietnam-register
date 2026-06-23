package com.example.frontend2.presentation.ui.home.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend2.presentation.ui.home.main.components.AutoScrollPager
import com.example.frontend2.presentation.ui.home.main.components.BannerSection
import com.example.frontend2.presentation.ui.home.main.components.GridSection
import com.example.frontend2.presentation.ui.home.main.components.banners
import com.example.frontend2.presentation.ui.home.main.components.gridItems

@Composable
fun MainScreen(
    navController: NavController
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    AutoScrollPager(pagerState, banners.size)

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { BannerSection(pagerState, banners) }
                item {
                    GridSection(gridItems) { item ->
                        when (item.title) {
                            "Đặt lịch hẹn" -> navController.navigate("inspection_center_look_up")
                            "Đăng ký phương tiện" -> navController.navigate("vehicle_register")
                            "Trung tâm đăng kiểm" -> navController.navigate("inspection_center_look_up")
                            "Tra cứu phạt nguội" -> navController.navigate("violation_lookup")
                        }
                    }
                }
                item {}
            }
        }
    }
}
