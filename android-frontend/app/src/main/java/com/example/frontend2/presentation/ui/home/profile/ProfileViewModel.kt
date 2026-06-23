package com.example.frontend2.presentation.ui.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.repository.UserRepository
import com.example.frontend2.domain.model.Profile
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val successMessage: String = ""
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: UserRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState(isLoading = true))
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    fun getMe() {
        _profileState.value = _profileState.value.copy(isLoading = true, error = "")

        viewModelScope.launch {
            try {
                when (val profileResource = profileRepository.getMe()) {
                    is Resource.Success -> {
                        val userResponse = profileResource.data

                        // Convert UserResponse to Profile
                        val profile = Profile(
                            id = userResponse.id,
                            fullName = userResponse.fullName,
                            dayOfBirth = userResponse.dayOfBirth,
                            gender = userResponse.gender,
                            phone = userResponse.phone,
                            email = userResponse.email,
                            isVerified = userResponse.isVerified,
                            createdAt = userResponse.createdAt
                        )

                        _profileState.value = _profileState.value.copy(
                            profile = profile,
                            isLoading = false,
                            error = "",
                        )
                    }

                    is Resource.Error -> {
                        _profileState.value = _profileState.value.copy(
                            isLoading = false,
                            error = profileResource.exception.message.toString()
                        )
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Đã xảy ra lỗi không xác định"
                )
            }
        }
    }

    fun clearMessages() {
        _profileState.value = _profileState.value.copy(error = "", successMessage = "")
    }

    init {
        getMe()
    }
}