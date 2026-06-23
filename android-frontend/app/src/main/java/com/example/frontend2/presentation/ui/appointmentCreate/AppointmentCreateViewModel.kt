package com.example.frontend2.presentation.ui.appointmentCreate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.request.AppointmentCreateRequest
import com.example.frontend2.data.repository.AppointmentRepository
import com.example.frontend2.data.repository.InspectionCenterRepository
import com.example.frontend2.data.repository.VehicleRepository
import com.example.frontend2.domain.model.Vehicle
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AppointmentCreateViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val vehicleRepository: VehicleRepository,
    private val inspectionCenterRepository: InspectionCenterRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentCreateState())
    val uiState: StateFlow<AppointmentCreateState> = _uiState.asStateFlow()

    fun loadVehicles() {
        viewModelScope.launch {
            if (!networkUtils.isNetworkAvailable()) {
                _uiState.update { it.copy(error = "Không có kết nối internet") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = vehicleRepository.getVehicles()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            vehicles = result.data,
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                                ?: "Không thể tải danh sách phương tiện"
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun loadInspectionCenter(centerId: String) {
        viewModelScope.launch {
            if (!networkUtils.isNetworkAvailable()) {
                _uiState.update { it.copy(error = "Không có kết nối internet") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = inspectionCenterRepository.getInspectionCenterById(centerId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            inspectionCenterName = result.data.name,
                            inspectionCenterId = centerId,
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                                ?: "Không thể tải thông tin trung tâm đăng kiểm"
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun setSelectedVehicle(vehicleId: Int?) {
        _uiState.update { it.copy(selectedVehicleId = vehicleId) }
    }

    fun setSelectedDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun setNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateAndCreateAppointment() {
        val currentState = _uiState.value

        // Validate required fields
        if (currentState.selectedVehicleId == null) {
            _uiState.update { it.copy(validationError = "Vui lòng chọn phương tiện") }
            return
        }

        if (currentState.selectedDate == null) {
            _uiState.update { it.copy(validationError = "Vui lòng chọn ngày đăng kiểm") }
            return
        }

        if (currentState.inspectionCenterId.isEmpty()) {
            _uiState.update { it.copy(validationError = "Không tìm thấy thông tin trung tâm đăng kiểm") }
            return
        }

        // Create appointment
        viewModelScope.launch {
            if (!networkUtils.isNetworkAvailable()) {
                _uiState.update { it.copy(error = "Không có kết nối internet") }
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true, error = null, validationError = null) }

            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = currentState.selectedDate.format(dateFormatter)

            val appointmentCreateRequest = AppointmentCreateRequest(
                vehicle_id = currentState.selectedVehicleId.toString(),
                center_id = currentState.inspectionCenterId,
                appointment_date = formattedDate,
                notes = currentState.notes
            )

            when (val result = appointmentRepository.createAppointment(appointmentCreateRequest)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            successMessage = "Đặt lịch đăng kiểm thành công!"
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            error = result.exception.message ?: "Không thể đặt lịch đăng kiểm"
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearValidationError() {
        _uiState.update { it.copy(validationError = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    // State class to keep track of all UI data
    data class AppointmentCreateState(
        val vehicles: List<Vehicle> = emptyList(),
        val selectedVehicleId: Int? = null,
        val inspectionCenterId: String = "",
        val inspectionCenterName: String = "",
        val selectedDate: LocalDate? = null,
        val notes: String = "",
        val isLoading: Boolean = false,
        val isSubmitting: Boolean = false,
        val error: String? = null,
        val validationError: String? = null,
        val successMessage: String? = null
    )
}