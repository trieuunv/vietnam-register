package com.example.frontend2.data.repository

import android.util.Log
import com.example.frontend2.data.network.datasource.AuthDataSource
import com.example.frontend2.data.pref.AuthPreferences
import com.example.frontend2.domain.model.AccessToken
import com.example.frontend2.util.Resource
import com.example.frontend2.util.TokenExpires
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val authPreferences: AuthPreferences
) {

    /**
     * Refreshes the access token using the refresh token stored in preferences.
     * If the refresh token is not available or the refresh operation fails,
     */
    suspend fun refreshToken(): AccessToken? {
        val refreshToken = authPreferences.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        val refreshTokenResponse = authDataSource.refreshToken(refreshToken)
        if (!refreshTokenResponse.success) {
            return null
        }

        val refreshTokenData = refreshTokenResponse.data

        if (refreshTokenData != null) {
            val expiresIn = refreshTokenData.expiresIn.toLongOrNull() ?: 0L
            val accessToken =
                AccessToken(refreshTokenData.accessToken, TokenExpires.toExpireAt(expiresIn))
            authPreferences.saveAccessToken(accessToken)

            return accessToken
        }

        return null
    }

    /**
     * Checks if the user is logged in by verifying if the access token is available and not expired.
     * If the access token is expired, it attempts to refresh it using the refresh token.
     */
    suspend fun login(email: String, password: String): Resource<Boolean> {
        val loginResponse = authDataSource.login(email, password)

        return when (loginResponse.success) {
            true -> {
                val loginData =
                    loginResponse.data ?: return Resource.Error(Exception("Đã xảy ra sự cố!"))

                val expiresIn = loginData.expiresIn.toLongOrNull() ?: 0L

                val accessToken =
                    AccessToken(loginData.accessToken, TokenExpires.toExpireAt(expiresIn))

                authPreferences.saveAccessToken(accessToken)
                authPreferences.saveRefreshToken(loginData.refreshToken)

                Resource.Success(true)
            }

            false -> {
                val firstError = loginResponse.errors?.entries?.firstOrNull()

                when {
                    firstError == null -> {
                        val userFriendlyMessage = when (loginResponse.message) {
                            "Invalid credentials" -> "Email hoặc mật khẩu không đúng!"
                            else -> loginResponse.message ?: "Đã xảy ra sự cố!"
                        }

                        return Resource.Error(Exception(userFriendlyMessage))
                    }

                    firstError.key == "credentials" -> {
                        return Resource.Error(Exception("Thông tin đăng nhập không chính xác!"))
                    }

                    else -> {
                        return Resource.Error(Exception("${firstError.key}: ${firstError.value}"))
                    }
                }
            }
        }
    }

    fun logout() {
        authPreferences.clearTokens()
    }

    /**
     * Registers a new user with the provided information.
     * If the registration is successful, it saves the access token and refresh token in preferences.
     */
    suspend fun register(
        userName: String,
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String
    ): Resource<Boolean> {
        val registerResponse =
            authDataSource.register(userName, email, password, fullName, phoneNumber)

        Log.d("exceptionAuthRepository", "registerResponse: $registerResponse")

        return when (registerResponse.success) {
            true -> {
                val registerData =
                    registerResponse.data ?: return Resource.Error(Exception("Đã xảy ra sự cố!"))

                val expiresIn = registerData.expiresIn.toLongOrNull() ?: 0L

                val accessToken =
                    AccessToken(registerData.accessToken, TokenExpires.toExpireAt(expiresIn))

                authPreferences.saveAccessToken(accessToken)
                authPreferences.saveRefreshToken(registerData.refreshToken)

                Resource.Success(true)
            }

            false -> {
                Resource.Error(Exception(registerResponse.message ?: "Lỗi đăng ký!"))
            }
        }
    }
}