package com.example.frontend2.data.network.interceptor

import com.example.frontend2.data.pref.AuthPreferences
import com.example.frontend2.data.repository.AuthRepository
import com.example.frontend2.domain.model.AccessToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var accessToken = authPreferences.getAccessToken()

        val token = accessToken?.token
        val request = createRequestWithAuth(originalRequest, token)
        val response = chain.proceed(request)

        if (response.code == 401) {
            response.close()
            accessToken = refreshToken()

            if (accessToken == null) {
                logout()
                // Sửa lỗi: Thiếu return tại đây, làm cho flow xử lý không đúng
                return chain.proceed(originalRequest)
            }

            accessToken.let {
                val retriedRequest = createRequestWithAuth(originalRequest, it.token)
                return chain.proceed(retriedRequest)
            }
        }

        return response
    }

    private fun createRequestWithAuth(request: Request, token: String?): Request {
        return if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
    }

    private fun refreshToken(): AccessToken? {
        return runBlocking {
            try {
                authRepository.refreshToken()
            } catch (e: Exception) {
                // Xử lý ngoại lệ khi refresh token thất bại
                println("Refresh token failed: ${e.message}")
                null
            }
        }
    }

    private fun logout() {
        authPreferences.clearTokens()
        println("LOGOUT!!!")
    }
}