package com.example.frontend2.presentation.ui.components.vehicle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.runtime.Composable
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.presentation.theme.iconCarColor

@Composable
fun VehicleTypeDropdown(
    vehicleTypes: List<VehicleResponse>,
    vehicleTypesError: String?,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    onReload: () -> Unit
) {
    val vehicleTypeOptions = vehicleTypes.map { it.id to it.name }

    VehicleDropdownField(
        label = "Loại phương tiện",
        selectedValue = selectedValue,
        options = vehicleTypeOptions,
        onValueChange = onValueChange,
        error = errorMessage,
        icon = Icons.Rounded.DirectionsCar,
        iconColor = iconCarColor
    )
    /*if (vehicleTypesError != null) {
        // Hiển thị lỗi nếu có
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Loại phương tiện",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = vehicleTypesError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onReload,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Tải lại")
            }
        }
    } else {
        // Chuyển đổi danh sách loại phương tiện từ API sang định dạng Pair<String, String>
        val vehicleTypeOptions = vehicleTypes.map { it.id to it.name }

        VehicleDropdownField(
            label = "Loại phương tiện",
            selectedValue = selectedValue,
            options = vehicleTypeOptions,
            onValueChange = onValueChange,
            error = errorMessage,
            icon = Icons.Rounded.DirectionsCar,
            iconColor = iconCarColor
        )
    }*/
}