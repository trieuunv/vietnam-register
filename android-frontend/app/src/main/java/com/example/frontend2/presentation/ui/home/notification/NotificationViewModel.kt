package com.example.frontend2.presentation.ui.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.Notification
import com.example.frontend2.data.repository.NotificationRepository
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val hasNextPage: Boolean = false
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications(showLoading: Boolean = true) {
        if (showLoading) {
            _uiState.update { it.copy(isLoading = true) }
        }

        viewModelScope.launch {
            when (val result = notificationRepository.getNotifications(1)) {
                is Resource.Success -> {
                    val data = result.data
                    _uiState.update {
                        it.copy(
                            notifications = data,
                            isLoading = false,
                            error = null,
                            currentPage = 1,
                            lastPage = 5, // Assuming meta data is not directly accessible here
                            hasNextPage = data.size >= 10 // Assuming 10 items per page
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _snackbarMessage.value = result.exception.message
                }

                is Resource.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun loadMoreNotifications() {
        val currentState = _uiState.value

        if (currentState.isLoading || !currentState.hasNextPage) {
            return
        }

        val nextPage = currentState.currentPage + 1

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = notificationRepository.getNotifications(nextPage)) {
                is Resource.Success -> {
                    val newNotifications = currentState.notifications + result.data
                    _uiState.update {
                        it.copy(
                            notifications = newNotifications,
                            isLoading = false,
                            error = null,
                            currentPage = nextPage,
                            hasNextPage = result.data.isNotEmpty() && result.data.size >= 10
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _snackbarMessage.value = result.exception.message
                }

                is Resource.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun markSnackbarMessageShown() {
        _snackbarMessage.value = null
    }
}