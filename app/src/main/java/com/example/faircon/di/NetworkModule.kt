package com.example.faircon.di

import com.example.faircon.util.Urls
import com.example.faircon.network.services.FairconService
import com.example.faircon.network.webSocket.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWebSocketOkhttp(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideWebSocketListener(): WebSocketListener {
        return WebSocketListener()
    }

    @Singleton
    @Provides
    fun provideESP8266APRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.ESP8266_AP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideFairconService(
        retrofit: Retrofit
    ): FairconService {
        return retrofit
            .create(FairconService::class.java)
    }
}