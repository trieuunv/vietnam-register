package com.example.frontend2.data.network.api

import com.example.frontend2.data.network.dto.response.ApiResponse
import com.example.frontend2.data.network.dto.response.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {

    /**
     * Fetches paginated notifications for the current user.
     *
     * @param page The page number (optional).
     * @return A [Response] containing an [ApiResponse] with [NotificationResponse].
     */
    @GET("common/notification")
    suspend fun getNotifications(
        @Query("page") page: Int? = 1
    ): Response<ApiResponse<NotificationResponse>>

}
