package com.example.frontend2.presentation.ui.vehicleEdit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import com.example.frontend2.data.network.dto.request.VehicleUpdateRequest
import com.example.frontend2.domain.model.VehicleErrorState
import com.example.frontend2.presentation.theme.iconNumbersColor
import com.example.frontend2.presentation.theme.iconQrCodeColor
import com.example.frontend2.presentation.theme.iconSettingsColor
import com.example.frontend2.presentation.ui.components.vehicle.VehicleInputField

@Composable
fun VehicleEditIdentificationForm(
    formState: VehicleUpdateRequest,
    errorState: VehicleErrorState,
    onValueChange: (VehicleUpdateRequest) -> Unit
) {
    // Biển số xe
    VehicleInputField(
        label = "Biển số xe",
        value = formState.registrationNumber,
        onValueChange = {
            onValueChange(formState.copy(registrationNumber = it))
        },
        error = errorState.errorRegistrationNumber,
        placeholder = "Ví dụ: 76A12345",
        icon = Icons.Rounded.Numbers,
        iconColor = iconNumbersColor
    )

    // Số khung
    VehicleInputField(
        label = "Số khung",
        value = formState.chassisNumber,
        onValueChange = {
            onValueChange(formState.copy(chassisNumber = it))
        },
        error = errorState.errorChassisNumber,
        placeholder = "Ví dụ: RLA123456789GTS",
        icon = Icons.Rounded.QrCode,
        iconColor = iconQrCodeColor
    )

    // Số máy
    VehicleInputField(
        label = "Số máy",
        value = formState.engineNumber,
        onValueChange = {
            onValueChange(formState.copy(engineNumber = it))
        },
        error = errorState.errorEngineNumber,
        placeholder = "Ví dụ: EN789654321ZTYU",
        icon = Icons.Rounded.Settings,
        iconColor = iconSettingsColor
    )
}