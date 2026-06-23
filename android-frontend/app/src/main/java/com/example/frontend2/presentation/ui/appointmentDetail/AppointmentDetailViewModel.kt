package com.example.frontend2.presentation.ui.appointmentDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse
import com.example.frontend2.data.repository.AppointmentRepository
import com.example.frontend2.util.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val network: Network
) : ViewModel() {

    // UI states
    private val _uiState =
        MutableStateFlow<AppointmentDetailUiState>(AppointmentDetailUiState.Initial)
    val uiState: StateFlow<AppointmentDetailUiState> = _uiState

    // Event handling
    sealed class AppointmentDetailEvent {
        data class FetchAppointmentDetail(val appointmentId: String) : AppointmentDetailEvent()
        object RefreshAppointmentDetail : AppointmentDetailEvent()
    }

    // UI state representation
    sealed class AppointmentDetailUiState {
        object Initial : AppointmentDetailUiState()
        object Loading : AppointmentDetailUiState()
        data class Success(val appointment: AppointmentManagerResponse) : AppointmentDetailUiState()
        data class Error(val message: String) : AppointmentDetailUiState()
    }

    // Keep track of the current appointment ID
    private var currentAppointmentId: String? = null

    fun onEvent(event: AppointmentDetailEvent) {
        when (event) {
            is AppointmentDetailEvent.FetchAppointmentDetail -> {
                currentAppointmentId = event.appointmentId
                fetchAppointmentDetail(event.appointmentId)
            }

            is AppointmentDetailEvent.RefreshAppointmentDetail -> {
                currentAppointmentId?.let { fetchAppointmentDetail(it) }
            }
        }
    }

    private fun fetchAppointmentDetail(appointmentId: String) {
        // Check for network connectivity first
        if (!network.isNetworkAvailable()) {
            _uiState.value =
                AppointmentDetailUiState.Error("Không có kết nối mạng. Vui lòng kiểm tra kết nối của bạn.")
            return
        }

        viewModelScope.launch {
            appointmentRepository.getAppointmentById(appointmentId)
                .onStart {
                    _uiState.value = AppointmentDetailUiState.Loading
                }
                .catch { exception ->
                    Log.e("AppointmentDetailViewModel", "Error fetching appointment", exception)
                    _uiState.value = AppointmentDetailUiState.Error(
                        exception.message ?: "Có lỗi xảy ra khi tải thông tin lịch hẹn"
                    )
                }
                .collect { appointment ->
                    _uiState.value = AppointmentDetailUiState.Success(appointment)
                }
        }
    }
}