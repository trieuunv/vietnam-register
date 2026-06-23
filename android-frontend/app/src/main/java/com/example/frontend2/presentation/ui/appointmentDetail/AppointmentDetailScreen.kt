package com.example.frontend2.presentation.ui.appointmentDetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(
    navController: NavController,
    appointmentId: String,
    viewModel: AppointmentDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lazyListState = rememberLazyListState()
    val appBarElevation by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemScrollOffset > 0 || lazyListState.firstVisibleItemIndex > 0) 4.dp else 0.dp
        }
    }
    // Fetch appointment details on first composition
    LaunchedEffect(appointmentId) {
        viewModel.onEvent(
            AppointmentDetailViewModel.AppointmentDetailEvent.FetchAppointmentDetail(
                appointmentId
            )
        )
    }

    // Handle UI state changes for error messages
    LaunchedEffect(uiState) {
        if (uiState is AppointmentDetailViewModel.AppointmentDetailUiState.Error) {
            val errorMessage =
                (uiState as AppointmentDetailViewModel.AppointmentDetailUiState.Error).message
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Chi tiết lịch hẹn") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is AppointmentDetailViewModel.AppointmentDetailUiState.Initial,
                is AppointmentDetailViewModel.AppointmentDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is AppointmentDetailViewModel.AppointmentDetailUiState.Success -> {
                    val appointment =
                        (uiState as AppointmentDetailViewModel.AppointmentDetailUiState.Success).appointment

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Appointment basic info card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Thông tin lịch hẹn",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow("Mã xác nhận:", appointment.confirmationCode)
                                InfoRow("Trạng thái:", getStatusVietnamese(appointment.status))
                                InfoRow(
                                    "Ngày hẹn:",
                                    formatDateTime(appointment.appointmentDate)
                                )
                                InfoRow("Ghi chú:", appointment.notes ?: "Không có ghi chú")
                            }
                        }

                        // Vehicle info card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Thông tin xe",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow("Biển số:", appointment.vehicle.registrationNumber)
                                InfoRow("Nhãn hiệu:", appointment.vehicle.brand)
                                InfoRow("Kiểu loại:", appointment.vehicle.model)
                                InfoRow(
                                    "Năm sản xuất:",
                                    appointment.vehicle.manufactureYear.toString()
                                )
                                InfoRow("Số khung:", appointment.vehicle.chassisNumber)
                                InfoRow("Số máy:", appointment.vehicle.engineNumber)
                                InfoRow("Màu sắc:", appointment.vehicle.color)
                                InfoRow("Số chỗ ngồi:", appointment.vehicle.seatCount.toString())
                            }
                        }

                        // Center info card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Thông tin trung tâm đăng kiểm",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow("Tên trung tâm:", appointment.center.name)
                                InfoRow("Mã trung tâm:", appointment.center.code)
                                InfoRow("Số điện thoại:", appointment.center.phone)
                                InfoRow("Email:", appointment.center.email)
                                InfoRow("Giám đốc:", appointment.center.directorName)
                            }
                        }
                    }
                }

                is AppointmentDetailViewModel.AppointmentDetailUiState.Error -> {
                    // Error UI - a message will be shown in the Snackbar
                    // We can also show some UI here if needed
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Không thể tải thông tin lịch hẹn",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Vui lòng thử lại sau",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        HorizontalDivider()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(
            dateTimeString.replace(".000000Z", ""),
            DateTimeFormatter.ISO_DATE_TIME
        )
        dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (e: Exception) {
        dateTimeString
    }
}

private fun getStatusVietnamese(status: String): String {
    return when (status.lowercase()) {
        "pending" -> "Chờ xử lý"
        "completed" -> "Đã hoàn thành"
        "canceled" -> "Đã hủy"
        else -> status
    }
}