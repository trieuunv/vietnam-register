package com.example.frontend2.presentation.ui.appointmentManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.AppointmentManagerResponse
import com.example.frontend2.data.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentManagerViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentManagerUiState())
    val uiState = _uiState.asStateFlow()

    // Selected tab state (0 = All, 1 = Pending, 2 = Completed)
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    init {
        getAllAppointments()
    }

    /**
     * Handle tab selection and load the appropriate data
     */
    fun onTabSelected(index: Int) {
        _selectedTab.value = index
        refreshData()
    }

    /**
     * Refresh data based on the current selected tab
     */
    fun refreshData() {
        when (_selectedTab.value) {
            0 -> getAllAppointments()
            1 -> getPendingAppointments()
            2 -> getCompletedAppointments()
        }
    }

    /**
     * Get all appointments
     */
    private fun getAllAppointments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            appointmentRepository.getAllAppointments()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Đã có lỗi xảy ra"
                        )
                    }
                }
                .collectLatest { appointments ->
                    _uiState.update {
                        it.copy(
                            appointments = appointments,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    /**
     * Get pending appointments
     */
    private fun getPendingAppointments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            appointmentRepository.getPendingAppointments()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Đã có lỗi xảy ra"
                        )
                    }
                }
                .collectLatest { appointments ->
                    _uiState.update {
                        it.copy(
                            appointments = appointments,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    /**
     * Get completed appointments
     */
    private fun getCompletedAppointments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            appointmentRepository.getCompletedAppointments()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Đã có lỗi xảy ra"
                        )
                    }
                }
                .collectLatest { appointments ->
                    _uiState.update {
                        it.copy(
                            appointments = appointments,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}

data class AppointmentManagerUiState(
    val appointments: List<AppointmentManagerResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)