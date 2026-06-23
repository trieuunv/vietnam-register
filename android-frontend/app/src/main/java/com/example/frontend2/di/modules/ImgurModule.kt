package com.example.frontend2.di.modules

import com.example.frontend2.data.network.api.ImgurApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImgurModule {

    @Provides
    @Singleton
    fun provideImgurApi(): ImgurApi {
        // The actual creation happens in ImgurDataSource
        // This is just to make Hilt aware of the dependency
        return retrofit2.Retrofit.Builder().baseUrl("https://api.imgur.com").build()
            .create(ImgurApi::class.java)
    }
}