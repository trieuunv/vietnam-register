package com.example.frontend2.presentation.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend2.presentation.navigation.Screen
import com.example.frontend2.presentation.ui.components.AuthButton
import com.example.frontend2.util.SnackbarController
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading.value
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(scope, snackbarHostState) }

    // Các FocusRequester để chuyển đổi trường nhập liệu khi nhấn "Next"
    val userNameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val fullNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }

    val registerFormState = viewModel.registerFormState.collectAsState()

    // Lấy các thông báo lỗi từ ViewModel
    val userNameError = viewModel.registerErrorState.value.userNameError
    val emailError = viewModel.registerErrorState.value.emailError
    val passwordError = viewModel.registerErrorState.value.passwordError
    val confirmPasswordError = viewModel.registerErrorState.value.confirmPasswordError
    val fullNameError = viewModel.registerErrorState.value.fullNameError
    val phoneNumberError = viewModel.registerErrorState.value.phoneError

    // Theo dõi các sự kiện từ ViewModel
    LaunchedEffect(viewModel.registrationEvent) {
        viewModel.registrationEvent?.let { event ->
            when (event) {
                is RegisterViewModel.RegistrationEvent.Success -> {
                    snackbarController.showSuccess(event.message)
                    // Delay 2 giây trước khi chuyển màn hình
                    delay(2000)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    viewModel.resetEvent()
                }

                is RegisterViewModel.RegistrationEvent.Error -> {
                    snackbarController.showError(event.message)
                    viewModel.resetEvent()
                }
            }
        }
    }

    // Hàm xử lý đăng ký
    val onRegisterClick = {
        focusManager.clearFocus()  // Ẩn bàn phím
        // Kiểm tra khi người dùng nhấn nút đăng ký
        viewModel.checkUserName(registerFormState.value.userName)
        viewModel.checkEmail(registerFormState.value.email)
        viewModel.checkPassword(registerFormState.value.password)
        viewModel.checkConfirmPassword(registerFormState.value.confirmPassword)
        viewModel.checkFullName(registerFormState.value.fullName)
        viewModel.checkPhoneNumber(registerFormState.value.phone)

        viewModel.register()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // Giao diện màn hình Đăng ký
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2196F3))
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Đẩy phần nền trắng xuống dưới
            // Khung đăng ký có thể scroll
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(6f / 7f)
                    .padding(18.dp)
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = 18.dp)
                ) {
                    // Tiêu đề màn hình
                    Text(
                        text = "Đăng ký",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Các trường nhập liệu
                    RegisterInputField(
                        value = registerFormState.value.userName,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(userName = it)
                            )
                        },
                        placeholder = "Tên đăng nhập",
                        keyboardType = KeyboardType.Text,
                        focusRequester = userNameFocusRequester,
                        nextFocusRequester = emailFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.userName.isNotBlank()) viewModel.checkUserName(
                                registerFormState.value.userName
                            )
                        },
                        isError = userNameError.isNotEmpty(),
                        errorText = userNameError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường Email
                    RegisterInputField(
                        value = registerFormState.value.email,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(
                                    email = it
                                )
                            )
                        },
                        placeholder = "Email",
                        keyboardType = KeyboardType.Email,
                        focusRequester = emailFocusRequester,
                        nextFocusRequester = passwordFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.email.isNotBlank()) viewModel.checkEmail(
                                registerFormState.value.email
                            )
                        },
                        isError = emailError.isNotEmpty(),
                        errorText = emailError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường Mật khẩu
                    RegisterInputField(
                        value = registerFormState.value.password,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(
                                    password = it
                                )
                            )
                        },
                        placeholder = "Mật khẩu",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        focusRequester = passwordFocusRequester,
                        nextFocusRequester = confirmPasswordFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.password.isNotBlank()) viewModel.checkPassword(
                                registerFormState.value.password
                            )
                        },
                        isError = passwordError.isNotEmpty(),
                        errorText = passwordError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường Xác nhận Mật khẩu
                    RegisterInputField(
                        value = registerFormState.value.confirmPassword,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(
                                    confirmPassword = it
                                )
                            )
                        },
                        placeholder = "Xác nhận mật khẩu",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        focusRequester = confirmPasswordFocusRequester,
                        nextFocusRequester = fullNameFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.confirmPassword.isNotBlank()) viewModel.checkConfirmPassword(
                                registerFormState.value.confirmPassword
                            )
                        },
                        isError = confirmPasswordError.isNotEmpty(),
                        errorText = confirmPasswordError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường Họ tên đầy đủ
                    RegisterInputField(
                        value = registerFormState.value.fullName,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(
                                    fullName = it
                                )
                            )
                        },
                        placeholder = "Họ tên đầy đủ",
                        keyboardType = KeyboardType.Text,
                        focusRequester = fullNameFocusRequester,
                        nextFocusRequester = phoneNumberFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.fullName.isNotBlank()) viewModel.checkFullName(
                                registerFormState.value.fullName
                            )
                        },
                        isError = fullNameError.isNotEmpty(),
                        errorText = fullNameError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trường Số điện thoại
                    RegisterInputField(
                        value = registerFormState.value.phone,
                        onValueChange = {
                            viewModel.updateRegisterFormState(
                                registerFormState.value.copy(
                                    phone = it
                                )
                            )
                        },
                        placeholder = "Số điện thoại",
                        keyboardType = KeyboardType.Phone,
                        focusRequester = phoneNumberFocusRequester,
                        onFocusChanged = {
                            if (!it.isFocused && registerFormState.value.phone.isNotBlank()) viewModel.checkPhoneNumber(
                                registerFormState.value.phone
                            )
                        },
                        isError = phoneNumberError.isNotEmpty(),
                        errorText = phoneNumberError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkbox đồng ý điều khoản
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = viewModel.isTermsAccepted.value,
                            onCheckedChange = { viewModel.updateTermsAccepted(it) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chấp nhận điều khoản sử dụng")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nút Đăng ký
                    AuthButton(
                        text = "Đăng ký",
                        isLoading = isLoading,
                        isEnabled = viewModel.isButtonEnabled,
                        onClick = onRegisterClick
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Liên kết đến màn hình Đăng nhập
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Đã có tài khoản?",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 16.sp
                        )
                        TextButton(
                            onClick = {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0D47A1))
                        ) {
                            Text(
                                text = "Đăng nhập",
                                color = MaterialTheme.colorScheme.inversePrimary,
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Thêm padding dưới cùng để tránh nút đăng ký bị che khi bàn phím hiện lên
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

// Trường nhập liệu chung (Email, Mật khẩu, v.v.)
@Composable
fun RegisterInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null,
    onFocusChanged: ((FocusState) -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                nextFocusRequester?.requestFocus()
            },
            onDone = {
                // Đóng bàn phím khi nhấn Done trên trường cuối cùng
                if (nextFocusRequester == null) {
                    // Không có focus requester tiếp theo, đây là trường cuối cùng
                }
            }
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester ?: FocusRequester.Default)
            .onFocusChanged { onFocusChanged?.invoke(it) }
    )

    if (isError && !errorText.isNullOrEmpty()) {
        Text(
            text = errorText,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}