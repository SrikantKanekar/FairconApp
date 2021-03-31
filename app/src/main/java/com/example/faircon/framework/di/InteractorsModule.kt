package com.example.faircon.framework.di

import com.example.faircon.business.interactors.account.AccountInteractors
import com.example.faircon.business.interactors.account.ChangePassword
import com.example.faircon.business.interactors.account.GetAccountProperties
import com.example.faircon.business.interactors.account.UpdateAccountProperties
import com.example.faircon.business.interactors.auth.AttemptLogin
import com.example.faircon.business.interactors.auth.AttemptRegistration
import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.business.interactors.auth.CheckPreviousUser
import com.example.faircon.business.interactors.home.ConnectToFaircon
import com.example.faircon.business.interactors.home.DisconnectFromFaircon
import com.example.faircon.business.interactors.home.HomeInteractors
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.datasource.network.services.AccountService
import com.example.faircon.framework.datasource.network.services.AuthService
import com.example.faircon.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @Provides
    fun provideAuthInteractors(
        authService: AuthService,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        emailDataStore: EmailDataStore
    ): AuthInteractors {
        return AuthInteractors(
            AttemptLogin(authTokenDao, accountPropertiesDao, authService, emailDataStore),
            AttemptRegistration(authTokenDao, accountPropertiesDao, authService, emailDataStore),
            CheckPreviousUser(authTokenDao, accountPropertiesDao, emailDataStore)
        )
    }

    @Provides
    fun provideAccountInteractors(
        accountService: AccountService,
        accountPropertiesDao: AccountPropertiesDao
    ): AccountInteractors {
        return AccountInteractors(
            GetAccountProperties(accountService, accountPropertiesDao),
            UpdateAccountProperties(accountService, accountPropertiesDao),
            ChangePassword(accountService)
        )
    }

    @Provides
    fun provideHomeInteractors(
        app: BaseApplication
    ): HomeInteractors {
        return HomeInteractors(
            ConnectToFaircon(app),
            DisconnectFromFaircon(app)
        )
    }
}