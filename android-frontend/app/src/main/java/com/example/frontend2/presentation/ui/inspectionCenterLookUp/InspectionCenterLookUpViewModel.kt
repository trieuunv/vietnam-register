package com.example.frontend2.presentation.ui.inspectionCenterLookUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.network.dto.response.DistrictResponse
import com.example.frontend2.data.network.dto.response.InspectionCenterResponse
import com.example.frontend2.data.network.dto.response.ProvinceResponse
import com.example.frontend2.data.network.dto.response.WardResponse
import com.example.frontend2.data.repository.InspectionCenterRepository
import com.example.frontend2.data.repository.ProvinceRepository
import com.example.frontend2.util.Network
import com.example.frontend2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionCenterLookUpViewModel @Inject constructor(
    private val inspectionCenterRepository: InspectionCenterRepository,
    private val networkUtils: Network,
    private val provinceRepository: ProvinceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationSelectionState())
    val uiState: StateFlow<LocationSelectionState> = _uiState.asStateFlow()

    init {
        loadProvinces()
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingProvinces = true) }

            when (val result = provinceRepository.getProvinces()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            provinces = result.data,
                            isLoadingProvinces = false,
                            provinceError = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingProvinces = false,
                            provinceError = result.exception.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoadingProvinces = true) }
                }
            }
        }
    }

    fun onProvinceSelected(province: ProvinceResponse) {
        _uiState.update {
            it.copy(
                selectedProvince = province,
                selectedDistrict = null,
                selectedWard = null,
                districts = emptyList(),
                wards = emptyList(),
                districtError = null,
                wardError = null,
                inspectionCenters = emptyList(),
                isLoadingCenters = false,
                centerError = null
            )
        }
        loadDistricts(province.code)
        // Load inspection centers with just province selected
        loadInspectionCenters(province.code, "", "")
    }

    private fun loadDistricts(provinceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingDistricts = true) }

            when (val result = provinceRepository.getDistricts(provinceId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            districts = result.data,
                            isLoadingDistricts = false,
                            districtError = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingDistricts = false,
                            districtError = result.exception.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoadingDistricts = true) }
                }
            }
        }
    }

    fun onDistrictSelected(district: DistrictResponse) {
        val province = _uiState.value.selectedProvince
        if (province != null) {
            _uiState.update {
                it.copy(
                    selectedDistrict = district,
                    selectedWard = null,
                    wards = emptyList(),
                    wardError = null
                )
            }
            loadWards(district.code)
            // Load inspection centers with province and district selected
            loadInspectionCenters(province.code, district.code, "")
        }
    }

    private fun loadWards(districtId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingWards = true) }

            when (val result = provinceRepository.getWards(districtId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            wards = result.data,
                            isLoadingWards = false,
                            wardError = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingWards = false,
                            wardError = result.exception.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoadingWards = true) }
                }
            }
        }
    }

    fun onWardSelected(ward: WardResponse) {
        val province = _uiState.value.selectedProvince
        val district = _uiState.value.selectedDistrict

        if (province != null && district != null) {
            _uiState.update {
                it.copy(selectedWard = ward)
            }
            // Load inspection centers with all three location parameters
            loadInspectionCenters(province.code, district.code, ward.code)
        }
    }

    // Function to load inspection centers and update UI state
    private fun loadInspectionCenters(provinceId: String, districtId: String, wardId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCenters = true, centerError = null) }

            when (val result = getInspectionCenters(provinceId, districtId, wardId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            inspectionCenters = result.data,
                            isLoadingCenters = false,
                            centerError = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            inspectionCenters = emptyList(),
                            isLoadingCenters = false,
                            centerError = result.exception.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoadingCenters = true) }
                }
            }
        }
    }

    // Function to get inspection centers
    private suspend fun getInspectionCenters(
        provinceId: String,
        districtId: String,
        wardId: String
    ): Resource<List<InspectionCenterResponse>> {
        // Check network connection
        if (!networkUtils.isNetworkAvailable()) {
            return Resource.Error(Exception("Không có kết nối internet"))
        }

        // Call repository
        return inspectionCenterRepository.getInspectionCenters(provinceId, districtId, wardId)
    }

    // State class to keep track of all UI data
    data class LocationSelectionState(
        val provinces: List<ProvinceResponse> = emptyList(),
        val districts: List<DistrictResponse> = emptyList(),
        val wards: List<WardResponse> = emptyList(),
        val inspectionCenters: List<InspectionCenterResponse> = emptyList(),

        val selectedProvince: ProvinceResponse? = null,
        val selectedDistrict: DistrictResponse? = null,
        val selectedWard: WardResponse? = null,

        val isLoadingProvinces: Boolean = false,
        val isLoadingDistricts: Boolean = false,
        val isLoadingWards: Boolean = false,
        val isLoadingCenters: Boolean = false,

        val provinceError: String? = null,
        val districtError: String? = null,
        val wardError: String? = null,
        val centerError: String? = null
    )
}