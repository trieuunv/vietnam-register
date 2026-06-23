package com.example.frontend2.presentation.ui.vehicleRegister

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.request.VehicleRegisterRequest
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.data.repository.VehicleRepository
import com.example.frontend2.domain.model.VehicleErrorState
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleRegisterViewmodel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _vehicleRegisterFormState =
        MutableStateFlow(VehicleRegisterRequest("", "", "", "", "", "", "", "", "", "", ""))
    val vehicleRegisterFormState: StateFlow<VehicleRegisterRequest> = _vehicleRegisterFormState

    private val _vehicleRegisterErrorState = mutableStateOf(VehicleErrorState())
    val vehicleRegisterErrorState: State<VehicleErrorState> get() = _vehicleRegisterErrorState

    private val _isRequestFormValidated = MutableStateFlow(false)
    private val isRequestFormValidated: StateFlow<Boolean> = _isRequestFormValidated

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    // Thêm trạng thái cho danh sách loại phương tiện từ API
    private val _vehicleTypes = mutableStateOf<List<VehicleResponse>>(emptyList())
    val vehicleTypes: State<List<VehicleResponse>> get() = _vehicleTypes

    // Thêm trạng thái loading cho việc tải loại phương tiện
    private val _isLoadingVehicleTypes = mutableStateOf(false)

    // Thêm trạng thái lỗi khi tải loại phương tiện
    private val _vehicleTypesError = mutableStateOf<String?>(null)
    val vehicleTypesError: State<String?> get() = _vehicleTypesError

    init {
        // Tải danh sách loại phương tiện khi ViewModel được khởi tạo
        fetchVehicleTypes()
    }

    private fun isNonBlank(value: String) = value.isNotBlank()

    private fun isValidSeatCount(seatCount: String): Boolean =
        seatCount.toIntOrNull()?.let { it > 0 } == true

    private fun isVehicleRegisterValidated() {
        val form = vehicleRegisterFormState.value
        val errors = VehicleErrorState(
            errorVehicleTypeId = if (!isNonBlank(form.vehicleTypeId)) "Loại phương tiện không hợp lệ" else "",
            errorRegistrationNumber = if (!isNonBlank(form.registrationNumber)) "Biển số không hợp lệ" else "",
            errorChassisNumber = if (!isNonBlank(form.chassisNumber)) "Số khung không hợp lệ" else "",
            errorEngineNumber = if (!isNonBlank(form.engineNumber)) "Số máy không hợp lệ" else "",
            errorBrand = if (!isNonBlank(form.brand)) "Hãng xe không hợp lệ" else "",
            errorModel = if (!isNonBlank(form.model)) "Mẫu xe không hợp lệ" else "",
            errorManufactureYear = if (!isNonBlank(form.manufactureYear)) "Năm sản xuất không hợp lệ" else "",
            errorColor = if (!isNonBlank(form.color)) "Màu sắc không hợp lệ" else "",
            errorSeatCount = if (!isValidSeatCount(form.seatCount)) "Số ghế không hợp lệ" else "",
            errorFirstRegistrationDate = if (!isNonBlank(form.firstRegistrationDate)) "Ngày đăng ký lần đầu không hợp lệ" else "",
            errorPurposeOfUse = if (!isNonBlank(form.purposeOfUse)) "Mục đích sử dụng không hợp lệ" else ""
        )
        _vehicleRegisterErrorState.value = errors
        _isRequestFormValidated.value = errors == VehicleErrorState()
    }

    fun updateVehicleFormState(form: VehicleRegisterRequest) {
        _vehicleRegisterFormState.value = form
    }

    // Thêm hàm để lấy danh sách loại phương tiện từ API
    fun fetchVehicleTypes() {
        if (!networkUtils.isNetworkAvailable()) {
            _vehicleTypesError.value =
                "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        viewModelScope.launch {
            _isLoadingVehicleTypes.value = true
            _vehicleTypesError.value = null

            try {
                when (val resource = vehicleRepository.getVehicleType()) {
                    is Resource.Success -> {
                        _vehicleTypes.value = resource.data
                        Log.d("VehicleRegister", "Loaded vehicle types: ${resource.data}")
                    }

                    is Resource.Error -> {
                        _vehicleTypesError.value = resource.exception.message
                            ?: "Không thể tải danh sách loại phương tiện"
                        Log.e(
                            "VehicleRegister",
                            "Error loading vehicle types: ${resource.exception.message}"
                        )
                    }

                    is Resource.Loading -> {}
                }
            } finally {
                _isLoadingVehicleTypes.value = false
            }
        }
    }

    fun vehicleRegister(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!networkUtils.isNetworkAvailable()) {
            onError("Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.")
            return
        }

        isVehicleRegisterValidated()
        if (!isRequestFormValidated.value) {
            onError("Vui lòng kiểm tra lại thông tin đăng ký.")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = vehicleRegisterFormState.value
                Log.d("VehicleRegister", "Sending request: $request")

                when (val resource = vehicleRepository.vehicleRegister(request)) {
                    is Resource.Success -> {
                        onSuccess("Đăng ký phương tiện thành công")
                    }

                    is Resource.Error -> {
                        handleServerErrors(resource.errorData)
                        onError(resource.exception.message ?: "Đã xảy ra lỗi! Vui lòng đăng ký lại")
                    }

                    is Resource.Loading -> {}
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleServerErrors(errorData: Any?) {
        if (errorData == null) return

        @Suppress("UNCHECKED_CAST")
        val serverErrors = errorData as? Map<String, List<String>> ?: return
        Log.e("VehicleRegister", "Server errors: $serverErrors")

        val currentErrors = _vehicleRegisterErrorState.value.copy()

        val genericErrorMessages = mapOf(
            "vehicle_type_id" to "Loại phương tiện không hợp lệ",
            "manufacture_year" to "Năm sản xuất không hợp lệ",
            "first_registration_date" to "Ngày đăng ký lần đầu không hợp lệ",
            "registration_number" to "Biển số không hợp lệ",
            "chassis_number" to "Số khung không hợp lệ",
            "engine_number" to "Số máy không hợp lệ",
            "brand" to "Hãng xe không hợp lệ",
            "model" to "Mẫu xe không hợp lệ",
            "color" to "Màu sắc không hợp lệ",
            "seat_count" to "Số ghế không hợp lệ",
            "purpose_of_use" to "Mục đích sử dụng không hợp lệ"
        )

        val errorFieldMapping = mapOf<String, (String) -> Unit>(
            "vehicle_type_id" to { msg -> currentErrors.errorVehicleTypeId = msg },
            "manufacture_year" to { msg -> currentErrors.errorManufactureYear = msg },
            "first_registration_date" to { msg -> currentErrors.errorFirstRegistrationDate = msg },
            "registration_number" to { msg -> currentErrors.errorRegistrationNumber = msg },
            "chassis_number" to { msg -> currentErrors.errorChassisNumber = msg },
            "engine_number" to { msg -> currentErrors.errorEngineNumber = msg },
            "brand" to { msg -> currentErrors.errorBrand = msg },
            "model" to { msg -> currentErrors.errorModel = msg },
            "color" to { msg -> currentErrors.errorColor = msg },
            "seat_count" to { msg -> currentErrors.errorSeatCount = msg },
            "purpose_of_use" to { msg -> currentErrors.errorPurposeOfUse = msg }
        )

        serverErrors.keys.forEach { key ->
            genericErrorMessages[key]?.let { errorMessage ->
                errorFieldMapping[key]?.invoke(errorMessage)
            }
        }

        _vehicleRegisterErrorState.value = currentErrors
    }
}