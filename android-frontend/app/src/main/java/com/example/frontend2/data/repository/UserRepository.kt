package com.example.frontend2.data.repository

import android.util.Log
import com.example.frontend2.data.network.datasource.UserDataSource
import com.example.frontend2.data.network.dto.request.UpdateProfileRequest
import com.example.frontend2.data.network.dto.response.UserResponse
import com.example.frontend2.domain.model.Profile
import com.example.frontend2.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
) {
    suspend fun getMe(): Resource<UserResponse> {
        val response = userDataSource.getMe()
        Log.d("exceptionUserDataSource", "getMe: $response")
        return when (response.success) {
            true -> {
                Resource.Success(
                    response.data ?: throw Exception("Không thể tải thông tin người dùng")
                )
            }

            false -> {
                Resource.Error(Exception(response.message ?: "Không thể tải thông tin người dùng"))
            }
        }
    }

    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Resource<Profile> {
        val response = userDataSource.updateProfile(updateProfileRequest)
        return when (response.success) {
            true -> {
                Resource.Success(
                    response.data ?: throw Exception("Không thể tải thông tin người dùng")
                )
            }

            false -> {
                Resource.Error(
                    Exception(
                        response.message ?: "Không thể cập nhật thông tin người dùng"
                    )
                )
            }
        }
    }
}
