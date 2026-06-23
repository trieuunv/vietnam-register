package com.example.frontend2.presentation.ui.vehicleRegister.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.BrandingWatermark
import androidx.compose.material.icons.rounded.AirlineSeatReclineNormal
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.domain.model.VehicleErrorState
import com.example.frontend2.presentation.theme.iconBrandColor
import com.example.frontend2.presentation.theme.iconEventColor
import com.example.frontend2.presentation.theme.iconModelColor
import com.example.frontend2.presentation.theme.iconPaletteColor
import com.example.frontend2.presentation.theme.iconSeatColor
import com.example.frontend2.presentation.ui.components.vehicle.VehicleInputField
import com.example.frontend2.presentation.ui.vehicleRegister.SectionTitle

@Composable
fun VehicleCharacteristicsForm(
    formState: VehicleRegisterRequest,
    errorState: VehicleErrorState,
    onValueChange: (VehicleRegisterRequest) -> Unit
) {
    // Tiêu đề
    SectionTitle(
        text = "Đặc điểm xe",
        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Row 1: Hãng xe & Mẫu xe
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            VehicleInputField(
                label = "Hãng xe",
                value = formState.brand,
                onValueChange = {
                    onValueChange(formState.copy(brand = it))
                },
                error = errorState.errorBrand,
                placeholder = "Ví dụ: Honda",
                icon = Icons.AutoMirrored.Rounded.BrandingWatermark,
                iconColor = iconBrandColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            VehicleInputField(
                label = "Mẫu xe",
                value = formState.model,
                onValueChange = {
                    onValueChange(formState.copy(model = it))
                },
                error = errorState.errorModel,
                placeholder = "Ví dụ: CRX",
                icon = Icons.Rounded.DirectionsCar,
                iconColor = iconModelColor
            )
        }
    }

    // Row 2: Năm sản xuất & Màu sắc
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) {
            VehicleInputField(
                label = "Năm sản xuất",
                value = formState.manufactureYear,
                onValueChange = {
                    onValueChange(formState.copy(manufactureYear = it))
                },
                error = errorState.errorManufactureYear,
                placeholder = "Ví dụ: 2025",
                icon = Icons.Rounded.Event,
                iconColor = iconEventColor,
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            VehicleInputField(
                label = "Màu sắc",
                value = formState.color,
                onValueChange = {
                    onValueChange(formState.copy(color = it))
                },
                error = errorState.errorColor,
                placeholder = "Ví dụ: Trắng",
                icon = Icons.Rounded.Palette,
                iconColor = iconPaletteColor
            )
        }
    }

    // Số ghế
    VehicleInputField(
        label = "Số ghế",
        value = formState.seatCount,
        onValueChange = {
            onValueChange(formState.copy(seatCount = it))
        },
        error = errorState.errorSeatCount,
        placeholder = "Ví dụ: 4",
        icon = Icons.Rounded.AirlineSeatReclineNormal,
        iconColor = iconSeatColor,
        keyboardType = KeyboardType.Number
    )
}