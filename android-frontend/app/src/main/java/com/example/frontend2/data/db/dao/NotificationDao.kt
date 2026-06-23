package com.example.frontend2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.frontend2.data.db.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications ORDER BY created_at DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Int): NotificationEntity?

    @Query("UPDATE notifications SET is_read = :isRead WHERE id = :id")
    suspend fun updateNotificationReadStatus(id: Int, isRead: Boolean)

    @Query("UPDATE notifications SET is_read = 1 WHERE is_read = 0")
    suspend fun markAllNotificationsAsRead(): Int

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    @Query("SELECT COUNT(*) FROM notifications WHERE is_read = 0")
    fun getUnreadNotificationsCount(): Flow<Int>

}