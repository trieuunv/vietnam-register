package com.example.frontend2.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.repository.AuthRepository
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkUtils: Network
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val isButtonEnabled: Boolean
        get() = email.value.isNotBlank() && password.value.isNotBlank()

    fun updateEmail(value: String) {
        email.value = value
    }

    fun updatePassword(value: String) {
        password.value = value
    }

    fun login(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // Kiểm tra kết nối mạng trước khi thực hiện đăng nhập
        if (!networkUtils.isNetworkAvailable()) {
            onError("Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email.value, password.value)
            Log.d("LoginViewModel", "Login result: $result")
            when (result) {
                is Resource.Success -> {
                    onSuccess("Đăng nhập thành công")
                    _isLoggedIn.value = true
                }

                is Resource.Error -> {
                    val errorMessage = result.exception?.message ?: "Đã xảy ra lỗi không xác định"
                    onError(errorMessage)
                    _isLoading.value = false
                }

                is Resource.Loading -> {}
            }

        }
    }
}