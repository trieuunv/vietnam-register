package com.example.frontend2.di.modules

import android.content.Context
import com.example.frontend2.data.network.interceptor.AuthInterceptor
import com.example.frontend2.util.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL =
        "https://2788-2001-ee1-db05-2220-d803-64d8-a620-ce89.ngrok-free.app/api/"

    private val acceptHeaderInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideOkHttpClientWithAuth(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(acceptHeaderInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("no_auth")
    fun provideOkHttpClientWithoutAuth(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(acceptHeaderInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofitWithAuth(@Named("auth") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @Named("no_auth")
    fun provideRetrofitWithoutAuth(@Named("no_auth") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(
        @ApplicationContext context: Context
    ): Network {
        return Network(context)
    }
}