package com.example.frontend2.data.repository

import com.example.frontend2.data.network.datasource.NotificationDataSource
import com.example.frontend2.data.network.dto.response.Notification
import com.example.frontend2.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) {

    suspend fun getNotifications(page: Int = 1): Resource<List<Notification>> {
        val notificationResponse = notificationDataSource.getNotifications(page)

        return when (notificationResponse.success) {
            true -> {
                Resource.Success(notificationResponse.data?.data ?: emptyList())
            }

            false -> {
                Resource.Error(
                    exception = Exception(
                        notificationResponse.message ?: "Không thể tải danh sách thông báo"
                    ),
                    errorData = notificationResponse.errors
                )
            }
        }
    }

}