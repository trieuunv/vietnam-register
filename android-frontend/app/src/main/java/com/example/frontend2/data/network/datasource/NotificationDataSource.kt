package com.example.frontend2.data.network.datasource

import android.util.Log
import com.example.frontend2.data.network.api.NotificationApi
import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.NotificationResponse
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NotificationDataSource @Inject constructor(
    @Named("auth") private val retrofit: Retrofit
) : BaseDataSource() {

    private val notificationApi: NotificationApi by lazy {
        retrofit.create(NotificationApi::class.java)
    }

    /**
     * Fetches paginated notifications from the API.
     *
     * @param page The page number (optional, default is 1).
     * @return An [ApiResponse] object containing the [NotificationResponse].
     */
    suspend fun getNotifications(page: Int = 1): ApiResponse<NotificationResponse> {
        return safeApiCall {
            val notificationResponse = notificationApi.getNotifications(page)
            Log.d("NotificationDataSource", "Fetched notifications: $notificationResponse")

            notificationResponse
        }
    }
}
