package com.example.frontend2.presentation.ui.register

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.repository.AuthRepository
import com.example.frontend2.domain.model.Register
import com.example.frontend2.domain.model.RegisterErrorState
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _registerFormState = MutableStateFlow(Register("", "", "", "", "", ""))
    val registerFormState: StateFlow<Register> = _registerFormState

    private val _registerErrorState = mutableStateOf(RegisterErrorState())
    val registerErrorState: State<RegisterErrorState> = _registerErrorState

    private val _isRegisterRequestFormValidated = MutableStateFlow(false)
    val isRegisterRequestFormValidated: StateFlow<Boolean> = _isRegisterRequestFormValidated

    val isTermsAccepted = mutableStateOf(false)

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Định nghĩa sealed class cho các sự kiện đăng ký
    sealed class RegistrationEvent {
        data class Success(val message: String) : RegistrationEvent()
        data class Error(val message: String) : RegistrationEvent()
    }

    // Theo dõi sự kiện đăng ký
    private val _registrationEvent = mutableStateOf<RegistrationEvent?>(null)
    val registrationEvent: RegistrationEvent? get() = _registrationEvent.value

    // Reset sự kiện sau khi xử lý
    fun resetEvent() {
        _registrationEvent.value = null
    }

    // Hàm validate cho các trường
    private fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean =
        password.length >= 8 &&
                password.matches(".*[A-Z].*".toRegex()) &&
                password.matches(".*[a-z].*".toRegex()) &&
                password.matches(".*[0-9].*".toRegex()) &&
                password.matches(".*[!@#$%^&*()].*".toRegex())

    private fun isValidPhoneNumber(phoneNumber: String): Boolean =
        phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }

    private fun isValidUserName(userName: String): Boolean =
        userName.isNotBlank() && userName.length >= 3

    private fun isValidFullName(fullName: String): Boolean =
        fullName.isNotBlank() && fullName.length >= 3

    // Kiểm tra form sau mỗi lần cập nhật
    private fun isRegisterFormValidated() {
        val userName = _registerFormState.value.userName
        val email = _registerFormState.value.email
        val password = _registerFormState.value.password
        val confirmPassword = _registerFormState.value.confirmPassword
        val fullName = _registerFormState.value.fullName
        val phoneNumber = _registerFormState.value.phone

        val isUserNameValid = isValidUserName(userName)
        val isEmailValid = isValidEmail(email)
        val isPasswordValid = isValidPassword(password)
        val isConfirmPasswordValid = password == confirmPassword
        val isFullNameValid = isValidFullName(fullName)
        val isPhoneNumberValid = isValidPhoneNumber(phoneNumber)

        _isRegisterRequestFormValidated.value = isUserNameValid &&
                isEmailValid &&
                isPasswordValid &&
                isConfirmPasswordValid &&
                isFullNameValid &&
                isPhoneNumberValid &&
                isTermsAccepted.value
    }

    val isButtonEnabled: Boolean
        get() = isRegisterRequestFormValidated.value

    fun updateRegisterFormState(register: Register) {
        _registerFormState.value = register
        // Cập nhật trạng thái validation mỗi khi form thay đổi
        isRegisterFormValidated()
    }

    // Hàm check khi người dùng nhập vào trường dữ liệu
    fun checkUserName(userName: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            userNameError = if (!isValidUserName(userName)) "Tên đăng nhập phải có ít nhất 3 ký tự" else ""
        )
        isRegisterFormValidated()
    }

    fun checkEmail(email: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            emailError = if (!isValidEmail(email)) "Email không hợp lệ" else ""
        )
        isRegisterFormValidated()
    }

    fun checkPassword(password: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            passwordError = if (!isValidPassword(password)) {
                "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt \"!@#$%^&*()\""
            } else ""
        )
        // Kiểm tra xác nhận mật khẩu mỗi khi mật khẩu thay đổi
        if (_registerFormState.value.confirmPassword.isNotBlank()) {
            checkConfirmPassword(_registerFormState.value.confirmPassword)
        }
        isRegisterFormValidated()
    }

    fun checkConfirmPassword(confirmPass: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            confirmPasswordError = if (_registerFormState.value.password != confirmPass) {
                "Mật khẩu xác nhận không khớp"
            } else ""
        )
        isRegisterFormValidated()
    }

    fun checkFullName(fullName: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            fullNameError = if (!isValidFullName(fullName)) {
                "Họ tên phải có ít nhất 3 ký tự"
            } else ""
        )
        isRegisterFormValidated()
    }

    fun checkPhoneNumber(phoneNumber: String) {
        _registerErrorState.value = _registerErrorState.value.copy(
            phoneError = if (!isValidPhoneNumber(phoneNumber)) {
                "Số điện thoại không hợp lệ, phải có 10 chữ số"
            } else ""
        )
        isRegisterFormValidated()
    }

    fun updateTermsAccepted(accepted: Boolean) {
        isTermsAccepted.value = accepted
        isRegisterFormValidated()
    }

    // Kiểm tra lỗi tổng thể trước khi đăng ký
    private fun hasErrors(): Boolean {
        return _registerErrorState.value.userNameError.isNotEmpty() ||
                _registerErrorState.value.emailError.isNotEmpty() ||
                _registerErrorState.value.passwordError.isNotEmpty() ||
                _registerErrorState.value.confirmPasswordError.isNotEmpty() ||
                _registerErrorState.value.fullNameError.isNotEmpty() ||
                _registerErrorState.value.phoneError.isNotEmpty() ||
                !isTermsAccepted.value
    }

    // Hàm đăng ký đã sửa lại để sử dụng sự kiện
    fun register() {
        // Kiểm tra kết nối mạng trước khi thực hiện đăng ký
        if (!networkUtils.isNetworkAvailable()) {
            _registrationEvent.value =
                RegistrationEvent.Error("Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.")
            return
        }

        // Kiểm tra lại form trước khi đăng ký
        checkUserName(_registerFormState.value.userName)
        checkEmail(_registerFormState.value.email)
        checkPassword(_registerFormState.value.password)
        checkConfirmPassword(_registerFormState.value.confirmPassword)
        checkFullName(_registerFormState.value.fullName)
        checkPhoneNumber(_registerFormState.value.phone)
        isRegisterFormValidated()

        if (hasErrors()) {
            _registrationEvent.value =
                RegistrationEvent.Error("Vui lòng kiểm tra lại thông tin đăng ký")
            return
        }

        if (!isTermsAccepted.value) {
            _registrationEvent.value =
                RegistrationEvent.Error("Vui lòng chấp nhận điều khoản sử dụng")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val resource = authRepository.register(
                _registerFormState.value.userName,
                _registerFormState.value.email,
                _registerFormState.value.password,
                _registerFormState.value.fullName,
                _registerFormState.value.phone
            )

            try {
                when (resource) {
                    is Resource.Success -> {
                        _registrationEvent.value = RegistrationEvent.Success("Đăng ký thành công")
                    }

                    is Resource.Error -> {
                        _registrationEvent.value = RegistrationEvent.Error(
                            resource.exception.message ?: "Đã xảy ra lỗi! Vui lòng đăng ký lại"
                        )
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _registrationEvent.value =
                    RegistrationEvent.Error("Đã xảy ra lỗi không mong muốn: ${e.message}")
            } finally {
                _isLoading.value = false // Tắt trạng thái loading
            }
        }
    }
}