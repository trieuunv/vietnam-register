package com.example.frontend2.presentation.ui.inspectionManager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.data.network.dto.response.InspectionsListResponse
import com.example.frontend2.presentation.navigation.Screen
import com.example.frontend2.util.SnackbarController
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionManagerScreen(
    navController: NavController,
    viewModel: InspectionManagerViewModel = hiltViewModel()
) {
    val inspectionsState by viewModel.inspectionsState.collectAsState()
    val searchQuery by viewModel.searchQuery

    val appBarElevation = 4.dp
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(coroutineScope, snackbarHostState) }

    // Check for errors and show them in the snackbar
    LaunchedEffect(inspectionsState) {
        if (inspectionsState is InspectionsState.Error) {
            snackbarController.showError((inspectionsState as InspectionsState.Error).message)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(appBarElevation),
                title = { Text(text = "Quản lý đăng kiểm") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.InspectionCenterLookUp.route) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = {
                    Text("Đặt lịch hẹn")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Tìm kiếm đăng kiểm...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Tìm kiếm"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Content based on state
            when (inspectionsState) {
                is InspectionsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is InspectionsState.Success -> {
                    val filteredInspections = viewModel.getFilteredInspections()

                    if (filteredInspections.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không tìm thấy kết quả",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        InspectionsList(
                            inspections = filteredInspections,
                            viewModel = viewModel,
                            onInspectionClick = { inspectionId ->
                                navController.navigate(
                                    Screen.InspectionDetail.route.replace(
                                        "{inspectionId}",
                                        inspectionId
                                    )
                                )
                            }
                        )
                    }
                }

                is InspectionsState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Lỗi",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = (inspectionsState as InspectionsState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InspectionsList(
    inspections: List<InspectionsListResponse>,
    viewModel: InspectionManagerViewModel,
    onInspectionClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(inspections) { inspection ->
            InspectionItem(
                inspection = inspection,
                formatDate = { viewModel.formatDate(it) },
                onClick = { onInspectionClick(inspection.id.toString()) }
            )
        }
    }
}

@Composable
fun InspectionItem(
    inspection: InspectionsListResponse,
    formatDate: (String) -> String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Certificate number and status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mã: ${inspection.certificateNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.weight(1f))

                val resultColor = when (inspection.result) {
                    "passed" -> Color.Green
                    "failed" -> Color.Red
                    "conditional" -> Color(0xFFFFA000)
                    else -> Color.Gray
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(resultColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatResult(inspection.result),
                        style = MaterialTheme.typography.bodyMedium,
                        color = resultColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vehicle information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Biển số: ${inspection.vehicle.registrationNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${inspection.vehicle.brand} ${inspection.vehicle.model}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (inspection.mileage != null) {
                    Text(
                        text = "${inspection.mileage} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // Dates and center information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Ngày đăng kiểm: ${formatDate(inspection.inspectionDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Ngày đăng kiểm tiếp theo: ${formatDate(inspection.nextInspectionDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Icon indicating inspection validity
                val currentDate = System.currentTimeMillis()
                val nextInspectionDate = try {
                    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateFormat.parse(inspection.nextInspectionDate.substring(0, 10))?.time ?: 0
                } catch (e: Exception) {
                    0
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Center information
            Text(
                text = "Nơi đăng kiểm: ${inspection.center.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


private fun formatResult(result: String?): String {
    return when (result?.lowercase()) {
        "conditional" -> "Đạt có điều kiện"
        "passed" -> "Đạt"
        "failed" -> "Không đạt"
        null -> "Chưa có kết quả"
        else -> result
    }
}