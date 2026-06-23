package com.example.frontend2.presentation.ui.inspectionCenterDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.InspectionCenterByIdResponse
import com.example.frontend2.data.repository.InspectionCenterRepository
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionCenterDetailViewModel @Inject constructor(
    private val inspectionCenterRepository: InspectionCenterRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionCenterDetailState())
    val uiState: StateFlow<InspectionCenterDetailState> = _uiState.asStateFlow()

    fun loadInspectionCenterDetail(centerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (!networkUtils.isNetworkAvailable()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Không có kết nối internet"
                    )
                }
                return@launch
            }

            when (val result = inspectionCenterRepository.getInspectionCenterById(centerId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            inspectionCenter = result.data,
                            isLoading = false,
                            error = null,
                            centerId = centerId
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

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    // State class to keep track of all UI data
    data class InspectionCenterDetailState(
        val inspectionCenter: InspectionCenterByIdResponse? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val centerId: String = ""
    )
}