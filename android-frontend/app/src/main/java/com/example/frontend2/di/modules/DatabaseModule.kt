package com.example.frontend2.di.modules

import android.content.Context
import androidx.room.Room
import com.example.frontend2.data.db.dao.AppointmentDao
import com.example.frontend2.data.db.dao.NotificationDao
import com.example.frontend2.data.db.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "vietnam-register"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    // Provider DAO

    @Provides
    @Singleton
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.notificationDao()
    }

    @Provides
    @Singleton
    fun provideAppointmentDao(database: AppDatabase): AppointmentDao {
        return database.appointmentDao()
    }
}