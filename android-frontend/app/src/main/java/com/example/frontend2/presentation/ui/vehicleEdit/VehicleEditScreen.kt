package com.example.frontend2.presentation.ui.vehicleEdit

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.presentation.navigation.Screen
import com.example.frontend2.presentation.ui.components.vehicle.VehicleButton
import com.example.frontend2.presentation.ui.components.vehicle.VehicleTypeDropdown
import com.example.frontend2.presentation.ui.vehicleEdit.components.VehicleEditCharacteristicsForm
import com.example.frontend2.presentation.ui.vehicleEdit.components.VehicleEditIdentificationForm
import com.example.frontend2.presentation.ui.vehicleEdit.components.VehicleEditRegistrationInfoDisplay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleEditScreen(
    navController: NavController,
    viewModel: VehicleEditViewModel = hiltViewModel()
) {
    val formState by viewModel.vehicleUpdateFormState.collectAsState()
    val errorState by viewModel.vehicleUpdateErrorState
    val isLoading by viewModel.isLoading
    val isLoadingDetails by viewModel.isLoadingVehicleDetails
    val vehicleDetails by viewModel.currentVehicle
    val vehicleDetailsError by viewModel.vehicleDetailsError
    val firstRegistrationDate by viewModel.firstRegistrationDate
    val vehicleTypes by viewModel.vehicleTypes
    val vehicleTypesError by viewModel.vehicleTypesError
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val appBarElevation by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemScrollOffset > 0 || lazyListState.firstVisibleItemIndex > 0) 4.dp else 0.dp
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Xử lý cập nhật phương tiện thành công
    val onVehicleUpdateClick = {
        viewModel.updateVehicle(
            onSuccess = { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
                navController.navigate(Screen.VehicleManager.route) {
                    popUpTo(Screen.VehicleManager.route) { inclusive = true }
                }
            },
            onError = { error ->
                scope.launch {
                    snackbarHostState.showSnackbar(error)
                }
            }
        )
    }

    LaunchedEffect(vehicleDetailsError) {
        vehicleDetailsError?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Cập nhật thông tin xe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                modifier = Modifier.shadow(appBarElevation)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
        ) {
            if (isLoadingDetails) {
                // Hiển thị trạng thái đang tải chi tiết phương tiện
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (vehicleDetailsError != null) {
                // Hiển thị thông báo lỗi khi không thể tải chi tiết phương tiện
                Text(
                    text = vehicleDetailsError ?: "Không thể tải thông tin phương tiện",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            } else if (vehicleDetails != null) {
                // Hiển thị form chỉnh sửa khi có dữ liệu
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Section 1: Thông tin phương tiện
                    item {
                        SectionTitle(
                            text = "Thông tin phương tiện",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        VehicleTypeDropdown(
                            vehicleTypes = vehicleTypes,
                            vehicleTypesError = vehicleTypesError,
                            selectedValue = formState.vehicleTypeId,
                            onValueChange = { value ->
                                viewModel.updateVehicleFormState(formState.copy(vehicleTypeId = value))
                            },
                            errorMessage = errorState.errorVehicleTypeId,
                            onReload = { viewModel.fetchVehicleTypes() }
                        )
                    }

                    item {
                        VehicleEditIdentificationForm(
                            formState = formState,
                            errorState = errorState,
                            onValueChange = { updatedForm ->
                                viewModel.updateVehicleFormState(updatedForm)
                            }
                        )
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    // Section 2: Đặc điểm xe
                    item {
                        VehicleEditCharacteristicsForm(
                            formState = formState,
                            errorState = errorState,
                            onValueChange = { updatedForm ->
                                viewModel.updateVehicleFormState(updatedForm)
                            }
                        )
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    // Section 3: Thông tin đăng ký (chỉ hiển thị, không chỉnh sửa)
                    item {
                        VehicleEditRegistrationInfoDisplay(
                            firstRegistrationDate = firstRegistrationDate,
                            purposeOfUse = formState.purposeOfUse,
                            errorPurposeOfUse = errorState.errorPurposeOfUse,
                            onPurposeOfUseChange = { newValue ->
                                viewModel.updateVehicleFormState(formState.copy(purposeOfUse = newValue))
                            }
                        )
                    }

                    // Button cập nhật
                    item {
                        VehicleButton(
                            isLoading = isLoading,
                            onClick = {
                                focusManager.clearFocus()
                                onVehicleUpdateClick()
                            }
                        )

                        // Thêm padding cuối cùng
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color
    )
}