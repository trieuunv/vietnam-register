package com.example.frontend2.presentation.ui.profileDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.request.UpdateProfileRequest
import com.example.frontend2.data.repository.UserRepository
import com.example.frontend2.domain.model.Profile
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun updateProfile(fullName: String, dayOfBirth: String, gender: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val updateRequest = UpdateProfileRequest(
                fullName = fullName,
                dayOfBirth = dayOfBirth,
                gender = gender
            )

            when (val result = userRepository.updateProfile(updateRequest)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            profile = result.data,
                            isLoading = false,
                            error = null,
                            isUpdateSuccess = true
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
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class ProfileDetailUiState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdateSuccess: Boolean = false
)