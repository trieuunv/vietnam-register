package com.example.frontend2.presentation.ui.inspectionManager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.InspectionsListResponse
import com.example.frontend2.data.repository.InspectionRepository
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InspectionManagerViewModel @Inject constructor(
    private val inspectionRepository: InspectionRepository
) : ViewModel() {

    // State for inspections list
    private val _inspectionsState = MutableStateFlow<InspectionsState>(InspectionsState.Loading)
    val inspectionsState: StateFlow<InspectionsState> = _inspectionsState.asStateFlow()

    // Search query state
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // Filter state
    private val _filterState = mutableStateOf(FilterState())
    val filterState: State<FilterState> = _filterState

    // Initialize by loading inspections
    init {
        loadInspections()
    }

    fun loadInspections() {
        _inspectionsState.value = InspectionsState.Loading
        viewModelScope.launch {
            when (val result = inspectionRepository.getAllInspections()) {
                is Resource.Success -> {
                    _inspectionsState.value = InspectionsState.Success(result.data)
                }

                is Resource.Error -> {
                    _inspectionsState.value = InspectionsState.Error(
                        result.exception.message ?: "Đã xảy ra lỗi không xác định"
                    )
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredInspections(): List<InspectionsListResponse> {
        val currentState = _inspectionsState.value
        if (currentState !is InspectionsState.Success) {
            return emptyList()
        }

        var filteredList = currentState.data

        // Apply search filter
        if (_searchQuery.value.isNotBlank()) {
            val query = _searchQuery.value.lowercase()
            filteredList = filteredList.filter { inspection ->
                inspection.certificateNumber.lowercase().contains(query) ||
                        inspection.vehicle.registrationNumber.lowercase().contains(query) ||
                        inspection.vehicle.brand.lowercase().contains(query) ||
                        inspection.vehicle.model.lowercase().contains(query)
            }
        }

        // Apply status filter
        _filterState.value.status?.let { status ->
            filteredList = filteredList.filter { it.status == status }
        }

        // Apply center filter
        _filterState.value.centerId?.let { centerId ->
            filteredList = filteredList.filter { it.centerId == centerId }
        }

        // Apply date range filter
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        _filterState.value.startDate?.let { startDate ->
            filteredList = filteredList.filter {
                try {
                    val inspectionDate = dateFormat.parse(it.inspectionDate.substring(0, 10))
                    inspectionDate != null && !inspectionDate.before(startDate)
                } catch (e: Exception) {
                    true
                }
            }
        }

        _filterState.value.endDate?.let { endDate ->
            filteredList = filteredList.filter {
                try {
                    val inspectionDate = dateFormat.parse(it.inspectionDate.substring(0, 10))
                    inspectionDate != null && !inspectionDate.after(endDate)
                } catch (e: Exception) {
                    true
                }
            }
        }

        return filteredList
    }

    // Format inspection date for display
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            try {
                // Try with a different format if the first one fails
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    dateString
                }
            } catch (e: Exception) {
                dateString
            }
        }
    }
}

// State for the inspections list
sealed class InspectionsState {
    object Loading : InspectionsState()
    data class Success(val data: List<InspectionsListResponse>) : InspectionsState()
    data class Error(val message: String) : InspectionsState()
}

// Filter state for the inspections
data class FilterState(
    val status: String? = null,
    val centerId: Int? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
)