package com.example.frontend2.data.db.mapper

import android.util.Log
import com.example.frontend2.data.db.entity.NotificationEntity
import com.example.frontend2.data.network.dto.response.Notification
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationMapper @Inject constructor() {

    /**
     * Maps a network [Notification] to a local [NotificationEntity]
     */
    fun mapToEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            id = notification.id,
            title = notification.title,
            content = notification.content,
            isRead = notification.isRead,
            createdAt = parseTimestamp(notification.createdAt)
        )
    }

    /**
     * Maps a list of network [Notification] to a list of local [NotificationEntity]
     */
    fun mapToEntityList(notifications: List<Notification>): List<NotificationEntity> {
        return notifications.map { mapToEntity(it) }
    }

    /**
     * Maps a local [NotificationEntity] to a network [Notification]
     * Bảo đảm các trường dữ liệu quan trọng như notificationType và relatedId
     * được giữ nguyên giữa chuyển đổi từ API sang local DB và ngược lại
     */
    fun mapFromEntity(entity: NotificationEntity): Notification {
        // Trích xuất notificationType và relatedId từ content nếu có
        val notificationType = extractNotificationType(entity.title)
        val relatedId = extractRelatedId(entity.content)

        return Notification(
            id = entity.id,
            userId = 0, // Default value
            title = entity.title,
            content = entity.content,
            notificationType = notificationType
                ?: "GENERAL", // Sử dụng giá trị mặc định nếu không trích xuất được
            isRead = entity.isRead,
            relatedId = relatedId ?: 0, // Sử dụng giá trị mặc định nếu không trích xuất được
            relatedType = "", // Default value
            createdAt = formatTimestamp(entity.createdAt)
        )
    }

    /**
     * Trích xuất notificationType từ tiêu đề thông báo
     * Dựa trên các tiêu đề phổ biến để xác định loại thông báo
     */
    private fun extractNotificationType(title: String): String? {
        return when {
            title.contains("lịch hẹn", ignoreCase = true) ||
                    title.contains("appointment", ignoreCase = true) -> "appointment"

            title.contains("kiểm định", ignoreCase = true) ||
                    title.contains("inspection", ignoreCase = true) -> "inspection"

            else -> null
        }
    }

    /**
     * Trích xuất relatedId từ nội dung thông báo
     * Tìm kiếm các mẫu số ID phổ biến trong nội dung
     */
    private fun extractRelatedId(content: String): Int? {
        // Tìm kiếm các mẫu như "ID: 123", "Id=123", "#123" trong nội dung
        val idPatterns = listOf(
            Regex("ID[:\\s=]\\s*(\\d+)"),
            Regex("Id[:\\s=]\\s*(\\d+)"),
            Regex("#(\\d+)"),
            Regex("mã[:\\s=]\\s*(\\d+)", RegexOption.IGNORE_CASE)
        )

        for (pattern in idPatterns) {
            val matchResult = pattern.find(content)
            if (matchResult != null) {
                val idStr = matchResult.groupValues[1]
                return idStr.toIntOrNull()
            }
        }

        return null
    }

    /**
     * Maps a list of local [NotificationEntity] to a list of network [Notification]
     */
    fun mapFromEntityList(entities: List<NotificationEntity>): List<Notification> {
        return entities.map { mapFromEntity(it) }
    }

    /**
     * Converts a timestamp string to a Long value
     */
    private fun parseTimestamp(timestamp: String): Long {
        return try {
            // Try parsing common ISO 8601 format
            val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .parse(timestamp)?.time

            if (parsedDate != null) {
                return parsedDate
            }

            // Try parsing timestamp as long directly
            timestamp.toLong()
        } catch (e: Exception) {
            Log.d("NotificationMapper", "Failed to parse timestamp: $timestamp", e)
            System.currentTimeMillis()
        }
    }

    /**
     * Formats a Long timestamp to a String
     */
    private fun formatTimestamp(timestamp: Long): String {
        return try {
            // Format as ISO 8601 for API compatibility
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(timestamp)
        } catch (e: Exception) {
            Log.d("NotificationMapper", "Failed to format timestamp: $timestamp", e)
            timestamp.toString()
        }
    }
}