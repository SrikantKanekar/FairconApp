package com.example.faircon.framework.di

import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.business.interactors.auth.CheckPreviousUser
import com.example.faircon.business.interactors.auth.AttemptLogin
import com.example.faircon.business.interactors.auth.AttemptRegistration
import com.example.faircon.business.interactors.main.account.AccountInteractors
import com.example.faircon.business.interactors.main.account.GetAccountProperties
import com.example.faircon.business.interactors.main.account.UpdateAccountProperties
import com.example.faircon.business.interactors.main.account.ChangePassword
import com.example.faircon.business.interactors.main.controller.*
import com.example.faircon.business.interactors.main.home.*
import com.example.faircon.framework.datasource.cache.authToken.AuthTokenDao
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.auth.AuthService
import com.example.faircon.framework.datasource.network.main.AccountService
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.main.ControllerService
import com.example.faircon.framework.datasource.network.main.HomeService
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
    fun provideControllerInteractors(
        controllerService: ControllerService,
        controllerDataStore: ControllerDataStore,
        wiFiLiveData: WiFiLiveData
    ): ControllerInteractors {
        return ControllerInteractors(
            SetFanSpeed(controllerService, controllerDataStore, wiFiLiveData),
            SetRequiredTemperature(controllerService, controllerDataStore, wiFiLiveData),
            SetTecVoltage(controllerService, controllerDataStore, wiFiLiveData)
        )
    }

    @Provides
    fun provideHomeInteractors(
        app: BaseApplication,
        controllerService: ControllerService,
        controllerDataStore: ControllerDataStore,
        homeService: HomeService,
        homeDataStore: HomeDataStore,
        wiFiLiveData: WiFiLiveData
    ): HomeInteractors {
        return HomeInteractors(
            SyncController(controllerService, controllerDataStore),
            GetParameters(homeService, homeDataStore),
            SetMode(homeService, homeDataStore, wiFiLiveData),
            ConnectToFaircon(app),
            DisconnectFromFaircon(app)
        )
    }
}