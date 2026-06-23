package com.example.frontend2.presentation.ui.vehicleManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.repository.VehicleRepository
import com.example.frontend2.domain.model.Vehicle
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleManagerViewmodel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val networkUtils: Network
) : ViewModel() {

    private val _vehicleList = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicleList: StateFlow<List<Vehicle>> = _vehicleList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _successMessage = MutableStateFlow("")
    val successMessage: StateFlow<String> = _successMessage

    fun loadVehicles() {
        if (!networkUtils.isNetworkAvailable()) {
            _errorMessage.value = "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                when (val result = vehicleRepository.getVehicles()) {
                    is Resource.Success -> {
                        result.data.let { vehicles ->
                            _vehicleList.value = vehicles
                        }
                    }

                    is Resource.Error -> {
                        _errorMessage.value =
                            result.exception.message ?: "Không thể tải danh sách phương tiện"
                        Log.e("VehicleManager", "Error loading vehicles", result.exception)
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi khi tải danh sách phương tiện"
                Log.e("VehicleManager", "Exception loading vehicles", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteVehicle(vehicleId: Int) {
        if (!networkUtils.isNetworkAvailable()) {
            _errorMessage.value = "Không có kết nối mạng. Vui lòng kiểm tra lại kết nối của bạn."
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                when (val result = vehicleRepository.deleteVehicle(vehicleId.toString())) {
                    is Resource.Success -> {
                        _vehicleList.value = _vehicleList.value.filter { it.id != vehicleId }
                        _successMessage.value = "Xóa phương tiện thành công"
                    }

                    is Resource.Error -> {
                        _errorMessage.value =
                            result.exception.message ?: "Không thể xóa phương tiện"
                        Log.e("VehicleManager", "Error deleting vehicle", result.exception)
                    }

                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi khi xóa phương tiện"
                Log.e("VehicleManager", "Exception deleting vehicle", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun clearSuccessMessage() {
        _successMessage.value = ""
    }
}