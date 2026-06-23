package com.example.frontend2.data.network.datasource

import android.util.Log
import com.example.frontend2.data.network.api.UserApi
import com.example.frontend2.data.network.dto.request.UpdateProfileRequest
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.UserResponse
import com.example.frontend2.domain.model.Profile
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    /**
     * Fetches the user profile from the API.
     *
     * @return [ApiResponse] containing the [Profile].
     */
    suspend fun getMe(): ApiResponse<UserResponse> {
        return safeApiCall { userApi.getMe() }
    }

    /**
     * Updates the user profile with the provided data.
     *
     * @param updateProfileRequest The updated profile data.
     * @return [ApiResponse] containing the updated [Profile].
     */
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): ApiResponse<Profile> {
        val profileResponse = userApi.updateProfile(updateProfileRequest)

        Log.d("exceptionUserDataSource", "updateProfile: $profileResponse")

        return safeApiCall {
            val response = userApi.updateProfile(updateProfileRequest)
            Log.d("exceptionUserDataSource", "updateProfile: $response")
            response
        }
    }

}