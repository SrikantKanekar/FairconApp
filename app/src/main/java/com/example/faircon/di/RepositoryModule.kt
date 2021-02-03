package com.example.faircon.di

import com.example.faircon.api.auth.AuthService
import com.example.faircon.api.main.MainService
import com.example.faircon.persistence.AccountPropertiesDao
import com.example.faircon.persistence.AuthTokenDao
import com.example.faircon.repository.auth.AuthRepository
import com.example.faircon.repository.auth.AuthRepositoryImpl
import com.example.faircon.repository.main.AccountRepository
import com.example.faircon.repository.main.AccountRepositoryImpl
import com.example.faircon.session.SessionManager
import com.example.faircon.util.MyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        authService: AuthService,
        myPreferences: MyPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(
            authTokenDao,
            accountPropertiesDao,
            authService,
            myPreferences
        )
    }

    @Singleton
    @Provides
    fun provideAccountRepository(
        mainService: MainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepositoryImpl(
            mainService,
            accountPropertiesDao,
            sessionManager
        )
    }
}