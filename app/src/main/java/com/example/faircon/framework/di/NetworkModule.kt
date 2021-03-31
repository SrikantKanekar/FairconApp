package com.example.faircon.framework.di

import com.example.faircon.business.domain.util.Urls
import com.example.faircon.framework.datasource.network.services.HomeService
import com.example.faircon.framework.datasource.network.webSocket.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideWebSocketOkhttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideWebSocketListener(): WebSocketListener {
        return WebSocketListener()
    }

    @Provides
    fun provideESP8266APRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.ESP8266_AP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideHomeService(
        retrofit: Retrofit
    ): HomeService {
        return retrofit
            .create(HomeService::class.java)
    }
}