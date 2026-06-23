package com.example.frontend2.presentation.ui.inspectionCenterLookUp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.data.network.dto.response.InspectionCenterResponse
import com.example.frontend2.util.SnackbarController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionCenterLookUpScreen(
    navController: NavController,
    viewModel: InspectionCenterLookUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(coroutineScope, snackbarHostState) }
    val appBarElevation = 4.dp

    // Dialog state management
    var showProvinceDialog by remember { mutableStateOf(false) }
    var showDistrictDialog by remember { mutableStateOf(false) }
    var showWardDialog by remember { mutableStateOf(false) }

    // Observe errors and show them as snackbars
    LaunchedEffect(
        uiState.centerError,
        uiState.provinceError,
        uiState.districtError,
        uiState.wardError
    ) {
        val errorMessage = uiState.centerError
            ?: uiState.provinceError
            ?: uiState.districtError
            ?: uiState.wardError

        errorMessage?.let {
            snackbarController.showError(it)
        }
    }

    // Render dialogs when state is true
    if (showProvinceDialog) {
        DropdownDialog(
            title = "Chọn Tỉnh/Thành phố",
            items = uiState.provinces,
            itemText = { it.name_with_type },
            onItemSelected = {
                viewModel.onProvinceSelected(it)
                showProvinceDialog = false
            },
            onDismiss = { showProvinceDialog = false }
        )
    }

    if (showDistrictDialog) {
        DropdownDialog(
            title = "Chọn Quận/Huyện",
            items = uiState.districts,
            itemText = { it.name_with_type },
            onItemSelected = {
                viewModel.onDistrictSelected(it)
                showDistrictDialog = false
            },
            onDismiss = { showDistrictDialog = false }
        )
    }

    if (showWardDialog) {
        DropdownDialog(
            title = "Chọn Phường/Xã",
            items = uiState.wards,
            itemText = { it.name_with_type },
            onItemSelected = {
                viewModel.onWardSelected(it)
                showWardDialog = false
            },
            onDismiss = { showWardDialog = false }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Tìm trung tâm đăng kiểm") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                modifier = Modifier.shadow(appBarElevation)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Province Dropdown
            LocationDropdown(
                title = "Tỉnh/Thành phố",
                selectedItem = uiState.selectedProvince?.name_with_type,
                isLoading = uiState.isLoadingProvinces,
                enabled = true,
                onClick = { showProvinceDialog = true }
            )

            // District Dropdown
            LocationDropdown(
                title = "Quận/Huyện",
                selectedItem = uiState.selectedDistrict?.name_with_type,
                isLoading = uiState.isLoadingDistricts,
                enabled = uiState.selectedProvince != null,
                onClick = { showDistrictDialog = true }
            )

            // Ward Dropdown
            LocationDropdown(
                title = "Phường/Xã",
                selectedItem = uiState.selectedWard?.name_with_type,
                isLoading = uiState.isLoadingWards,
                enabled = uiState.selectedDistrict != null,
                onClick = { showWardDialog = true }
            )

            // Results section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoadingCenters -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "Đang tìm kiếm trung tâm đăng kiểm...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    uiState.inspectionCenters.isNotEmpty() -> {
                        Column {
                            Text(
                                text = "Danh sách trung tâm đăng kiểm",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.inspectionCenters) { center ->
                                    InspectionCenterItem(
                                        center = center,
                                        onCenterClick = {
                                            coroutineScope.launch {
                                                navController.navigate("inspection_center_detail/${center.id}")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    uiState.selectedProvince != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Không tìm thấy trung tâm đăng kiểm",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Thử chọn khu vực khác",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Hãy chọn địa điểm để tìm trung tâm đăng kiểm",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> DropdownDialog(
    title: String,
    items: List<T>,
    itemText: (T) -> String,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredItems = items.filter {
        itemText(it).contains(searchText, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Tìm kiếm") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(filteredItems) { item ->
                        Text(
                            text = itemText(item),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemSelected(item) }
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun LocationDropdown(
    title: String,
    selectedItem: String?,
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled && !isLoading) { onClick() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = if (enabled) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isLoading) "Đang tải..."
                    else selectedItem ?: "Chọn $title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedItem != null) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun InspectionCenterItem(
    center: InspectionCenterResponse,
    onCenterClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCenterClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = center.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Mã trung tâm: ${center.code}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Địa chỉ",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = center.fullAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Status tag
            val (statusColor, statusBgColor) = when (center.status.lowercase()) {
                "active" -> Pair(Color.Green, Color.Green.copy(alpha = 0.1f))
                "busy" -> Pair(Color(0xFFFFA500), Color(0xFFFFA500).copy(alpha = 0.1f))
                "inactive" -> Pair(Color.Red, Color.Red.copy(alpha = 0.1f))
                else -> Pair(Color.Gray, Color.Gray.copy(alpha = 0.1f))
            }

            val statusText = when (center.status.lowercase()) {
                "active" -> "Đang hoạt động"
                "busy" -> "Bận"
                "inactive" -> "Không hoạt động"
                else -> center.status
            }

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = statusBgColor,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}