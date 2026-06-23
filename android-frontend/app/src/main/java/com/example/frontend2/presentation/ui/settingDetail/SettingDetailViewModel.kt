package com.example.frontend2.presentation.ui.settingDetail

import androidx.lifecycle.ViewModel
import com.example.frontend2.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun logout(
        onLogout: () -> Unit
    ) {
        authRepository.logout()
        onLogout()
    }
}