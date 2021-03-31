package com.example.faircon.framework.di

import com.example.faircon.business.domain.util.Urls
import com.example.faircon.framework.datasource.network.interceptors.TokenInterceptor
import com.example.faircon.framework.datasource.network.services.AccountService
import com.example.faircon.framework.datasource.network.services.AuthService
import com.example.faircon.framework.datasource.network.services.HomeService
import com.example.faircon.framework.datasource.network.webSocket.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @TokenOkHttp
    fun provideTokenOkhttp(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @WebSocketOkHttp
    fun provideWebSocketOkhttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideWebSocketListener(): WebSocketListener {
        return WebSocketListener()
    }

    @Provides
    @TokenRetrofit
    fun provideTokenRetrofit(
        @TokenOkHttp okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @NormalRetrofit
    fun provideNormalRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @ESP8266AP
    fun provideESP8266APRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.ESP8266_AP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideAuthService(
        @NormalRetrofit retrofit: Retrofit
    ): AuthService {
        return retrofit
            .create(AuthService::class.java)
    }

    @Provides
    fun provideAccountService(
        @TokenRetrofit retrofit: Retrofit
    ): AccountService {
        return retrofit
            .create(AccountService::class.java)
    }

    @Provides
    fun provideHomeService(
        @ESP8266AP retrofit: Retrofit
    ): HomeService {
        return retrofit
            .create(HomeService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketOkHttp


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ESP8266AP