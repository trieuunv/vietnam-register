package com.example.frontend2.presentation.ui.home.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend2.R

data class GridItemData(
    val imageRes: Int,
    val title: String,
    val subtitle: String
)

val gridItems = listOf(
    GridItemData(
        R.drawable.home_screen_grid_item_icon1,
        "Đặt lịch hẹn",
        "Đặt lịch đăng kiểm xe định kỳ, xe mới"
    ),
    GridItemData(
        R.drawable.home_screen_grid_item_icon2,
        "Đăng ký phương tiện",
        "Đăng ký thông tin xe"
    ),
    GridItemData(
        R.drawable.home_screen_grid_item_icon3,
        "Trung tâm đăng kiểm",
        "Danh sách trung tâm đăng kiểm"
    ),
    GridItemData(
        R.drawable.home_screen_grid_item_icon4,
        "Tra cứu phạt nguội",
        "Tra cứu thông tin phạt nguội, vi phạm"
    )
)

@Composable
fun GridItem(
    item: GridItemData,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 24.dp)
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = item.subtitle,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun GridSection(
    items: List<GridItemData>,
    onItemClick: (GridItemData) -> Unit
) {
    Spacer(modifier = Modifier.height(8.dp))
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(345.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size) { index ->
            GridItem(
                item = items[index],
                onClick = { onItemClick(items[index]) }
            )
        }
    }
}