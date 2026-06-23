package com.example.frontend2.presentation.ui.vehicleEdit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend2.presentation.theme.iconBusinessColor
import com.example.frontend2.presentation.theme.iconCalendarColor
import com.example.frontend2.presentation.ui.components.vehicle.VehicleInputField
import com.example.frontend2.presentation.ui.vehicleEdit.SectionTitle
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun VehicleEditRegistrationInfoDisplay(
    firstRegistrationDate: String?,
    purposeOfUse: String,
    errorPurposeOfUse: String,
    onPurposeOfUseChange: (String) -> Unit
) {
    // Tiêu đề
    SectionTitle(
        text = "Thông tin đăng ký",
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Hiển thị ngày đăng ký đầu tiên (không chỉnh sửa được)
    if (firstRegistrationDate != null) {
        RegistrationDateDisplay(
            firstRegistrationDate = firstRegistrationDate.formatDateTime()
        )
    }

    // Mục đích sử dụng (có thể chỉnh sửa)
    VehicleInputField(
        label = "Mục đích sử dụng",
        value = purposeOfUse,
        onValueChange = onPurposeOfUseChange,
        error = errorPurposeOfUse,
        placeholder = "Ví dụ: Cá nhân",
        icon = Icons.Rounded.Business,
        iconColor = iconBusinessColor
    )
}

fun String.formatDateTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

@Composable
fun RegistrationDateDisplay(
    firstRegistrationDate: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ngày đăng ký lần đầu",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = firstRegistrationDate,
            onValueChange = { },
            placeholder = { Text("YYYY-MM-DD") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    Icons.Rounded.DateRange,
                    contentDescription = null,
                    tint = iconCalendarColor
                )
            },
            singleLine = true
        )

        // Thêm thông báo để người dùng biết rằng không thể chỉnh sửa trường này
        Text(
            text = "Ngày đăng ký lần đầu không thể thay đổi",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
        )
    }
}
