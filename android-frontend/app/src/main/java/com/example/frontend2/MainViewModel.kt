package com.example.frontend2

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend2.data.pref.AuthPreferences
import com.example.frontend2.data.repository.AuthRepository
import com.example.frontend2.util.TokenExpires
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> get() = _isLoggedIn

    init {
        checkAccessToken()
    }

    private fun checkAccessToken() {
        val accessToken = authPreferences.getAccessToken()

        if (accessToken != null && TokenExpires.checkExpire(accessToken.expireAt)) {
            _isLoggedIn.value = true
        } else {
            refreshToken()
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            val accessToken = authRepository.refreshToken()
            _isLoggedIn.value = accessToken != null
        }
    }
}