package com.example.frontend2.presentation.ui.vehicleRegister

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.presentation.navigation.Screen
import com.example.frontend2.presentation.ui.components.vehicle.VehicleButton
import com.example.frontend2.presentation.ui.components.vehicle.VehicleTypeDropdown
import com.example.frontend2.presentation.ui.vehicleRegister.components.VehicleCharacteristicsForm
import com.example.frontend2.presentation.ui.vehicleRegister.components.VehicleIdentificationForm
import com.example.frontend2.presentation.ui.vehicleRegister.components.VehicleRegistrationInfoForm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleRegisterScreen(
    navController: NavController,
    viewModel: VehicleRegisterViewmodel = hiltViewModel()
) {
    val formState by viewModel.vehicleRegisterFormState.collectAsState()
    val errorState by viewModel.vehicleRegisterErrorState
    val isLoading by viewModel.isLoading
    val vehicleTypes by viewModel.vehicleTypes
    val vehicleTypesError by viewModel.vehicleTypesError
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val appBarElevation by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemScrollOffset > 0 || lazyListState.firstVisibleItemIndex > 0) 4.dp else 0.dp
        }
    }

    // Xử lý đăng ký phương tiện thành công
    val onVehicleRegisterClick = {
        viewModel.vehicleRegister(
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Đăng ký xe") },
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
                    VehicleIdentificationForm(
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
                    VehicleCharacteristicsForm(
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

                // Section 3: Thông tin đăng ký
                item {
                    VehicleRegistrationInfoForm(
                        formState = formState,
                        errorState = errorState,
                        onValueChange = { updatedForm ->
                            viewModel.updateVehicleFormState(updatedForm)
                        }
                    )
                }

                // Button đăng ký
                item {
                    VehicleButton(
                        isLoading = isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            onVehicleRegisterClick()
                        }
                    )

                    // Thêm padding cuối cùng
                    Spacer(modifier = Modifier.height(16.dp))
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

@Composable
fun AnimatedVisibility(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.animateContentSize()
    ) {
        if (visible) {
            content()
        }
    }
}