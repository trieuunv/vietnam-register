package com.example.frontend2.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frontend2.data.db.dao.AppointmentDao
import com.example.frontend2.data.db.dao.NotificationDao
import com.example.frontend2.data.db.entity.AppointmentEntity
import com.example.frontend2.data.db.entity.CenterEntity
import com.example.frontend2.data.db.entity.NotificationEntity
import com.example.frontend2.data.db.entity.VehicleEntity

@Database(
    entities = [
        NotificationEntity::class,
        AppointmentEntity::class,
        VehicleEntity::class,
        CenterEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    abstract fun appointmentDao(): AppointmentDao
}