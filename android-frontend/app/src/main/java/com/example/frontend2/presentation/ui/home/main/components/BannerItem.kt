package com.example.frontend2.presentation.ui.home.main.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.frontend2.R
import kotlinx.coroutines.delay

data class BannerItemData(
    val imageRes: Int,
    val url: String
)

val banners = listOf(
    BannerItemData(R.drawable.home_screen_banner_1, "https://www.1.com"),
    BannerItemData(R.drawable.home_screen_banner_2, "https://www.2.com"),
    BannerItemData(R.drawable.home_screen_banner_3, "https://www.3.com"),
    BannerItemData(R.drawable.home_screen_banner_4, "https://www.4.com"),
    BannerItemData(R.drawable.home_screen_banner_5, "https://www.5.com"),
    BannerItemData(R.drawable.home_screen_banner_6, "https://www.6.com"),
    BannerItemData(R.drawable.home_screen_banner_7, "https://www.7.com")
)

@Composable
fun BannerItem(
    banner: BannerItemData,
    scale: Float,
    context: Context
) {
    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, banner.url.toUri())
                context.startActivity(intent)
            }
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = banner.imageRes),
            contentDescription = "Banner",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun BannerSection(
    pagerState: androidx.compose.foundation.pager.PagerState,
    banners: List<BannerItemData>
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            pageSpacing = 16.dp,
            key = { banners[it].url }
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = 1f - (0.1f * kotlin.math.abs(pageOffset))

            BannerItem(
                banner = banners[page],
                scale = scale,
                context = context
            )
        }

        BannerIndicator(pagerState.currentPage, banners.size)
    }
}

@Composable
fun AutoScrollPager(pagerState: androidx.compose.foundation.pager.PagerState, pageCount: Int) {
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }
}

@Composable
fun BannerIndicator(currentPage: Int, size: Int) {

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) { index ->
            val isSelected = currentPage == index
            Canvas(
                modifier = Modifier.size(if (isSelected) 12.dp else 8.dp)
            ) {
                drawCircle(
                    color = if (isSelected) Color.White else Color.LightGray
                )
            }
        }
    }
}