package com.example.faircon.framework.di

import com.example.faircon.framework.datasource.network.services.AuthService
import com.example.faircon.framework.datasource.network.services.AccountService
import com.example.faircon.business.domain.util.Urls
import com.example.faircon.framework.datasource.network.interceptors.TokenInterceptor
import com.example.faircon.framework.datasource.network.services.ControllerService
import com.example.faircon.framework.datasource.network.services.HomeService
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
    fun provideOkhttpClient(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @TokenRetrofit
    fun provideTokenRetrofit(
        okHttpClient: OkHttpClient
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
    fun provideControllerService(
        @ESP8266AP retrofit: Retrofit
    ): ControllerService {
        return retrofit
            .create(ControllerService::class.java)
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
annotation class TokenRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ESP8266AP
