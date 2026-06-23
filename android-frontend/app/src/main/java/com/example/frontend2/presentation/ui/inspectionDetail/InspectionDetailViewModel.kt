package com.example.frontend2.presentation.ui.inspectionDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.InspectionByIdResponse
import com.example.frontend2.data.repository.InspectionRepository
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InspectionDetailViewModel @Inject constructor(
    private val inspectionRepository: InspectionRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionDetailState())
    val uiState: StateFlow<InspectionDetailState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun loadInspectionDetails(inspectionId: String) {
        viewModelScope.launch {
            if (!networkUtils.isNetworkAvailable()) {
                _uiState.update { it.copy(error = "Không có kết nối internet") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = inspectionRepository.getInspectionById(inspectionId)) {
                is Resource.Success -> {
                    processInspectionData(result.data)
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Không thể tải thông tin đăng kiểm"
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun processInspectionData(inspectionData: InspectionByIdResponse) {
        try {
            // Format dates
            val inspectionDateStr = inspectionData.inspectionDate.substring(0, 10)
            val nextInspectionDateStr = inspectionData.nextInspectionDate.substring(0, 10)

            val inspectionDate = dateFormat.parse(inspectionDateStr)
            val nextInspectionDate = dateFormat.parse(nextInspectionDateStr)

            val formattedInspectionDate =
                if (inspectionDate != null) displayDateFormat.format(inspectionDate) else ""
            val formattedNextInspectionDate =
                if (nextInspectionDate != null) displayDateFormat.format(nextInspectionDate) else ""

            // Count passed and failed criteria
            val criteriaResults = inspectionData.criteriaResults
            val passedCount =
                criteriaResults.count { it.result.equals("passed", ignoreCase = true) }
            val failedCount =
                criteriaResults.count { it.result.equals("failed", ignoreCase = true) }

            _uiState.update {
                it.copy(
                    inspection = inspectionData,
                    formattedInspectionDate = formattedInspectionDate,
                    formattedNextInspectionDate = formattedNextInspectionDate,
                    passedCriteriaCount = passedCount,
                    failedCriteriaCount = failedCount,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Lỗi xử lý dữ liệu: ${e.localizedMessage}"
                )
            }
        }
    }

    fun navigateToPayment() {
        // Will be implemented to handle payment navigation
        _uiState.update { it.copy(successMessage = "Đang chuyển đến trang thanh toán...") }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    data class InspectionDetailState(
        val inspection: InspectionByIdResponse? = null,
        val formattedInspectionDate: String = "",
        val formattedNextInspectionDate: String = "",
        val passedCriteriaCount: Int = 0,
        val failedCriteriaCount: Int = 0,
        val isLoading: Boolean = false,
        val error: String? = null,
        val successMessage: String? = null
    )
}