package com.example.frontend2.presentation.ui.citizenCard

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.request.CitizenCardRequest
import com.example.frontend2.data.repository.CitizenCardRepository
import com.example.frontend2.data.repository.ImgurRepository
import com.example.frontend2.domain.model.CitizenCardErrorState
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CitizenCardViewModel @Inject constructor(
    private val citizenCardRepository: CitizenCardRepository,
    private val imgurRepository: ImgurRepository,
    private val networkUtils: Network
) : ViewModel() {

    // Giữ lại URI hình ảnh tạm thời
    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> get() = _imageUri

    // Request được gửi lên API
    private val _citizenCardFormState = MutableStateFlow(
        CitizenCardRequest("", "", "", "", "")
    )
    val citizenCardFormState: StateFlow<CitizenCardRequest> = _citizenCardFormState

    // State lỗi cho form
    private val _citizenCardErrorState = mutableStateOf(CitizenCardErrorState())
    val citizenCardErrorState: State<CitizenCardErrorState> get() = _citizenCardErrorState

    // State cho việc validate form
    private val _isRequestFormValidated = MutableStateFlow(false)

    // State loading khi gửi request
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    // State loading khi upload ảnh
    private val _isImageUploading = mutableStateOf(false)
    val isImageUploading: State<Boolean> get() = _isImageUploading

    // State thành công
    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> get() = _isSuccess

    // State thông báo lỗi
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    fun onCitizenIdChanged(value: String) {
        val currentForm = _citizenCardFormState.value
        _citizenCardFormState.value = currentForm.copy(citizenId = value)
    }

    fun onPlaceOfOriginChanged(value: String) {
        val currentForm = _citizenCardFormState.value
        _citizenCardFormState.value = currentForm.copy(placeOfOrigin = value)
    }

    fun onDateOfIssueChanged(value: String) {
        val currentForm = _citizenCardFormState.value
        _citizenCardFormState.value = currentForm.copy(dateOfIssue = value)
    }

    fun onPlaceOfIssueChanged(value: String) {
        val currentForm = _citizenCardFormState.value
        _citizenCardFormState.value = currentForm.copy(placeOfIssue = value)
    }

    fun onImageSelected(uri: Uri) {
        _imageUri.value = uri
    }

    // New method to clear the error message after it's been displayed
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // New method to clear form field errors after they've been displayed
    fun clearFieldErrors() {
        _citizenCardErrorState.value = CitizenCardErrorState()
    }

    fun submitCitizenCard(context: Context) {
        // Kiểm tra kết nối mạng
        if (!networkUtils.isNetworkAvailable()) {
            _errorMessage.value = "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        // Kiểm tra form
        if (!validateForm()) {
            // Instead of returning silently, we'll show the first error in a snackbar
            val errors = _citizenCardErrorState.value
            val firstError = listOfNotNull(
                errors.errorCitizenId.takeIf { it.isNotEmpty() },
                errors.errorPlaceOfOrigin.takeIf { it.isNotEmpty() },
                errors.errorDateOfIssue.takeIf { it.isNotEmpty() },
                errors.errorPlaceOfIssue.takeIf { it.isNotEmpty() },
                errors.errorImageUrl.takeIf { it.isNotEmpty() }
            ).firstOrNull()

            firstError?.let {
                _errorMessage.value = it
            }
            return
        }

        // Kiểm tra nếu có URI hình ảnh
        val uri = _imageUri.value
        if (uri == null) {
            _errorMessage.value = "Vui lòng tải lên hình ảnh CMND/CCCD"
            return
        }

        // Upload ảnh lên Imgur
        uploadImage(context, uri)
    }

    private fun validateForm(): Boolean {
        val form = _citizenCardFormState.value
        val errors = CitizenCardErrorState(
            errorCitizenId = if (form.citizenId.isBlank()) "Vui lòng nhập số CMND/CCCD" else "",
            errorPlaceOfOrigin = if (form.placeOfOrigin.isBlank()) "Vui lòng nhập quê quán" else "",
            errorDateOfIssue = if (form.dateOfIssue.isBlank()) "Vui lòng nhập ngày cấp" else "",
            errorPlaceOfIssue = if (form.placeOfIssue.isBlank()) "Vui lòng nhập nơi cấp" else "",
            errorImageUrl = if (_imageUri.value == null) "Vui lòng tải lên hình ảnh CMND/CCCD" else ""
        )

        _citizenCardErrorState.value = errors
        _isRequestFormValidated.value = errors == CitizenCardErrorState()

        return _isRequestFormValidated.value
    }

    private fun uploadImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                _isImageUploading.value = true
                _errorMessage.value = null

                // Chuyển đổi Uri thành File
                val file = createFileFromUri(context, uri)
                if (file == null) {
                    _isImageUploading.value = false
                    _errorMessage.value = "Không thể xử lý ảnh. Vui lòng thử lại"
                    return@launch
                }

                // Upload lên Imgur
                val result = imgurRepository.uploadImage(file, "Citizen Card ID")
                result.fold(
                    onSuccess = { response ->
                        // Cập nhật URL ảnh trong form request
                        val currentForm = _citizenCardFormState.value
                        _citizenCardFormState.value =
                            currentForm.copy(imageUrl = response.upload.link)
                        _isImageUploading.value = false

                        // Tiếp tục gửi request API
                        sendCitizenCardRequest()
                    },
                    onFailure = { exception ->
                        _isImageUploading.value = false
                        _errorMessage.value = "Lỗi khi tải ảnh lên: ${exception.message}"
                        Log.e("CitizenCardViewModel", "Image upload error", exception)
                    }
                )
            } catch (e: Exception) {
                _isImageUploading.value = false
                _errorMessage.value = "Đã xảy ra lỗi: ${e.message}"
                Log.e("CitizenCardViewModel", "Error in uploadImage", e)
            }
        }
    }

    private fun sendCitizenCardRequest() {
        val form = _citizenCardFormState.value

        if (form.imageUrl.isBlank()) {
            _errorMessage.value = "URL hình ảnh không hợp lệ. Vui lòng thử lại"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = citizenCardRepository.citizenCard(form)
                Log.d("CitizenCardViewModel", "API result: $result")
                when (result) {
                    is Resource.Success -> {
                        _isLoading.value = false
                        _isSuccess.value = true
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value =
                            result.exception.message ?: "Đã xảy ra lỗi khi xác thực danh tính"
                        handleServerErrors(result.errorData)
                        Log.e("CitizenCardViewModel", "API error", result.exception)
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Đã xảy ra lỗi: ${e.message}"
                Log.e("CitizenCardViewModel", "Error in sendCitizenCardRequest", e)
            }
        }
    }

    private fun handleServerErrors(errorData: Any?) {
        if (errorData == null) return

        @Suppress("UNCHECKED_CAST")
        val serverErrors = errorData as? Map<String, List<String>> ?: return
        Log.e("CitizenCardViewModel", "Server errors: $serverErrors")

        // Instead of setting individual field errors, collect all errors to show in snackbar
        val allErrors = mutableListOf<String>()

        val errorMapping = mapOf(
            "citizen_id" to "Số CMND/CCCD",
            "place_of_origin" to "Quê quán",
            "date_of_issue" to "Ngày cấp",
            "place_of_issue" to "Nơi cấp",
            "image_url" to "Hình ảnh"
        )

        serverErrors.forEach { (key, messages) ->
            errorMapping[key]?.let { fieldName ->
                messages.forEach { message ->
                    allErrors.add("$fieldName: $message")
                }
            }
        }

        if (allErrors.isNotEmpty()) {
            _errorMessage.value = allErrors.first()
        }
    }

    // Helper function to convert Uri to File
    private fun createFileFromUri(context: Context, uri: Uri): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "citizen_card_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            FileOutputStream(file).use { outputStream ->
                inputStream.use { input ->
                    input.copyTo(outputStream)
                }
            }

            return file
        } catch (e: Exception) {
            Log.e("CitizenCardViewModel", "Error creating file from Uri", e)
            return null
        }
    }
}