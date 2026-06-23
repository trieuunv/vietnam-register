package com.example.frontend2.presentation.ui.vehicleEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.request.VehicleUpdateRequest
import com.example.frontend2.data.network.dto.response.VehicleResponse
import com.example.frontend2.data.repository.VehicleRepository
import com.example.frontend2.domain.model.Vehicle
import com.example.frontend2.domain.model.VehicleErrorState
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleEditViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val networkUtils: Network,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vehicleId: String = savedStateHandle.get<String>("vehicleId") ?: ""

    // Form state cho chỉnh sửa phương tiện
    private val _vehicleUpdateFormState =
        MutableStateFlow(VehicleUpdateRequest("", "", "", "", "", "", "", "", "", ""))
    val vehicleUpdateFormState: StateFlow<VehicleUpdateRequest> = _vehicleUpdateFormState

    // Thông tin về ngày đăng ký đầu tiên (chỉ hiển thị, không chỉnh sửa)
    private val _firstRegistrationDate = mutableStateOf("")
    val firstRegistrationDate: State<String> = _firstRegistrationDate

    // Vehicle hiện tại đang chỉnh sửa
    private val _currentVehicle = mutableStateOf<Vehicle?>(null)
    val currentVehicle: State<Vehicle?> = _currentVehicle

    private val _vehicleUpdateErrorState = mutableStateOf(VehicleErrorState())
    val vehicleUpdateErrorState: State<VehicleErrorState> = _vehicleUpdateErrorState

    private val _isRequestFormValidated = MutableStateFlow(false)
    private val isRequestFormValidated: StateFlow<Boolean> = _isRequestFormValidated

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Trạng thái khi đang tải thông tin phương tiện
    private val _isLoadingVehicleDetails = mutableStateOf(false)
    val isLoadingVehicleDetails: State<Boolean> = _isLoadingVehicleDetails

    // Thêm trạng thái cho danh sách loại phương tiện từ API
    private val _vehicleTypes = mutableStateOf<List<VehicleResponse>>(emptyList())
    val vehicleTypes: State<List<VehicleResponse>> = _vehicleTypes

    // Thêm trạng thái lỗi khi tải loại phương tiện
    private val _vehicleTypesError = mutableStateOf<String?>(null)
    val vehicleTypesError: State<String?> = _vehicleTypesError

    // Lỗi khi tải thông tin chi tiết phương tiện
    private val _vehicleDetailsError = mutableStateOf<String?>(null)
    val vehicleDetailsError: State<String?> = _vehicleDetailsError

    init {
        // Tải danh sách loại phương tiện và chi tiết phương tiện hiện tại
        fetchVehicleTypes()
        if (vehicleId.isNotBlank()) {
            fetchVehicleDetails(vehicleId)
        }
    }

    private fun isNonBlank(value: String) = value.isNotBlank()

    private fun isValidSeatCount(seatCount: String): Boolean =
        seatCount.toIntOrNull()?.let { it > 0 } == true

    private fun isVehicleUpdateValidated() {
        val form = vehicleUpdateFormState.value
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
            errorPurposeOfUse = if (!isNonBlank(form.purposeOfUse)) "Mục đích sử dụng không hợp lệ" else ""
        )
        _vehicleUpdateErrorState.value = errors
        _isRequestFormValidated.value = errors == VehicleErrorState()
    }

    fun updateVehicleFormState(form: VehicleUpdateRequest) {
        _vehicleUpdateFormState.value = form
    }

    // Lấy thông tin chi tiết phương tiện từ API
    private fun fetchVehicleDetails(vehicleId: String) {
        if (!networkUtils.isNetworkAvailable()) {
            _vehicleDetailsError.value =
                "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        viewModelScope.launch {
            _isLoadingVehicleDetails.value = true
            _vehicleDetailsError.value = null

            try {
                when (val resource = vehicleRepository.getVehicleById(vehicleId)) {
                    is Resource.Success -> {
                        val vehicle = resource.data
                        _currentVehicle.value = vehicle

                        // Lưu trữ ngày đăng ký đầu tiên (chỉ hiển thị)
                        _firstRegistrationDate.value = vehicle.firstRegistrationDate ?: ""

                        // Thiết lập giá trị form từ dữ liệu phương tiện
                        _vehicleUpdateFormState.value = VehicleUpdateRequest(
                            vehicleTypeId = vehicle.vehicleTypeId?.toString() ?: "",
                            registrationNumber = vehicle.registrationNumber,
                            chassisNumber = vehicle.chassisNumber ?: "",
                            engineNumber = vehicle.engineNumber ?: "",
                            brand = vehicle.brand,
                            model = vehicle.model ?: "",
                            manufactureYear = vehicle.manufactureYear?.toString() ?: "",
                            color = vehicle.color,
                            seatCount = vehicle.seatCount?.toString() ?: "",
                            purposeOfUse = vehicle.purposeOfUse ?: ""
                        )

                        Log.d("VehicleEdit", "Loaded vehicle details: $vehicle")
                    }

                    is Resource.Error -> {
                        _vehicleDetailsError.value = resource.exception.message
                            ?: "Không thể tải thông tin chi tiết phương tiện"
                        Log.e(
                            "VehicleEdit",
                            "Error loading vehicle details: ${resource.exception.message}"
                        )
                    }

                    is Resource.Loading -> {}
                }
            } finally {
                _isLoadingVehicleDetails.value = false
            }
        }
    }

    // Hàm để lấy danh sách loại phương tiện từ API
    fun fetchVehicleTypes() {
        if (!networkUtils.isNetworkAvailable()) {
            _vehicleTypesError.value =
                "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        viewModelScope.launch {
            _vehicleTypesError.value = null

            try {
                when (val resource = vehicleRepository.getVehicleType()) {
                    is Resource.Success -> {
                        _vehicleTypes.value = resource.data
                        Log.d("VehicleEdit", "Loaded vehicle types: ${resource.data}")
                    }

                    is Resource.Error -> {
                        _vehicleTypesError.value = resource.exception.message
                            ?: "Không thể tải danh sách loại phương tiện"
                        Log.e(
                            "VehicleEdit",
                            "Error loading vehicle types: ${resource.exception.message}"
                        )
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _vehicleTypesError.value =
                    e.message ?: "Đã xảy ra lỗi khi tải danh sách loại phương tiện"
            }
        }
    }

    fun updateVehicle(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!networkUtils.isNetworkAvailable()) {
            onError("Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn.")
            return
        }

        isVehicleUpdateValidated()
        if (!isRequestFormValidated.value) {
            onError("Vui lòng kiểm tra lại thông tin cập nhật.")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = vehicleUpdateFormState.value
                Log.d("VehicleEdit", "Sending update request: $request")

                when (val resource = vehicleRepository.updateVehicleById(vehicleId, request)) {
                    is Resource.Success -> {
                        onSuccess("Cập nhật phương tiện thành công")
                    }

                    is Resource.Error -> {
                        handleServerErrors(resource.errorData)
                        onError(resource.exception.message ?: "Đã xảy ra lỗi! Vui lòng thử lại")
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
        Log.e("VehicleEdit", "Server errors: $serverErrors")

        val currentErrors = _vehicleUpdateErrorState.value.copy()

        val genericErrorMessages = mapOf(
            "vehicle_type_id" to "Loại phương tiện không hợp lệ",
            "manufacture_year" to "Năm sản xuất không hợp lệ",
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

        _vehicleUpdateErrorState.value = currentErrors
    }
}