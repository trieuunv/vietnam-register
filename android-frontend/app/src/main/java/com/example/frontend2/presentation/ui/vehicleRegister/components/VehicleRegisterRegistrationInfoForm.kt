package com.example.frontend2.presentation.ui.vehicleRegister.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.domain.model.VehicleErrorState
import com.example.frontend2.presentation.theme.iconBusinessColor
import com.example.frontend2.presentation.theme.iconCalendarColor
import com.example.frontend2.presentation.ui.components.vehicle.VehicleInputField
import com.example.frontend2.presentation.ui.vehicleRegister.SectionTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VehicleRegistrationInfoForm(
    formState: VehicleRegisterRequest,
    errorState: VehicleErrorState,
    onValueChange: (VehicleRegisterRequest) -> Unit
) {
    // Tiêu đề
    SectionTitle(
        text = "Thông tin đăng ký",
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Chọn ngày đăng ký
    DatePickerField(
        value = formState.firstRegistrationDate,
        onValueChange = { date ->
            onValueChange(formState.copy(firstRegistrationDate = date))
        },
        label = "Ngày đăng ký lần đầu",
        errorMessage = errorState.errorFirstRegistrationDate,
        placeholder = "Ví dụ: 2025-05-14"
    )

    // Mục đích sử dụng
    VehicleInputField(
        label = "Mục đích sử dụng",
        value = formState.purposeOfUse,
        onValueChange = {
            onValueChange(formState.copy(purposeOfUse = it))
        },
        error = errorState.errorPurposeOfUse,
        placeholder = "Ví dụ: Cá nhân",
        icon = Icons.Rounded.Business,
        iconColor = iconBusinessColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String,
    placeholder: String
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (errorMessage.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        Icons.Rounded.DateRange,
                        contentDescription = "Chọn ngày",
                        tint = if (errorMessage.isNotEmpty()) MaterialTheme.colorScheme.error else iconCalendarColor
                    )
                }
            },
            singleLine = true,
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            onValueChange(formatter.format(date))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
