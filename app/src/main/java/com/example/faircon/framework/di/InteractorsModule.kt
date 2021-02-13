package com.example.faircon.framework.di

import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.business.interactors.auth.CheckPreviousUser
import com.example.faircon.business.interactors.auth.Login
import com.example.faircon.business.interactors.auth.Registration
import com.example.faircon.business.interactors.main.account.AccountInteractors
import com.example.faircon.business.interactors.main.account.GetAccountProperties
import com.example.faircon.business.interactors.main.account.SaveAccountProperties
import com.example.faircon.business.interactors.main.account.UpdatePassword
import com.example.faircon.framework.datasource.cache.authToken.AuthTokenDao
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.network.auth.AuthService
import com.example.faircon.framework.datasource.network.main.MainService
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Singleton
    @Provides
    fun provideAuthInteractors(
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        authService: AuthService,
        emailDataStore: EmailDataStore
    ): AuthInteractors{
        return AuthInteractors(
            Login(authTokenDao, accountPropertiesDao, authService, emailDataStore),
            Registration(authTokenDao, accountPropertiesDao, authService, emailDataStore),
            CheckPreviousUser(authTokenDao, accountPropertiesDao, emailDataStore)
        )
    }

    @Singleton
    @Provides
    fun provideAccountInteractors(
        mainService: MainService,
        accountPropertiesDao: AccountPropertiesDao
    ): AccountInteractors {
        return AccountInteractors(
            GetAccountProperties(mainService, accountPropertiesDao),
            SaveAccountProperties(mainService, accountPropertiesDao),
            UpdatePassword(mainService)
        )
    }
}