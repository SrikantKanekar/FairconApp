package com.example.faircon.framework.di

import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.business.interactors.auth.CheckPreviousUser
import com.example.faircon.business.interactors.auth.AttemptLogin
import com.example.faircon.business.interactors.auth.AttemptRegistration
import com.example.faircon.business.interactors.account.AccountInteractors
import com.example.faircon.business.interactors.account.GetAccountProperties
import com.example.faircon.business.interactors.account.UpdateAccountProperties
import com.example.faircon.business.interactors.account.ChangePassword
import com.example.faircon.business.interactors.controller.*
import com.example.faircon.business.interactors.home.*
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.services.AuthService
import com.example.faircon.framework.datasource.network.services.AccountService
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.services.ControllerService
import com.example.faircon.framework.datasource.network.services.HomeService
import com.example.faircon.framework.datasource.network.mappers.ParameterMapper
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
        wiFiLiveData: WiFiLiveData,
        parameterMapper: ParameterMapper
    ): HomeInteractors {
        return HomeInteractors(
            SyncController(controllerService, controllerDataStore),
            GetParameters(homeService, homeDataStore, parameterMapper),
            SetMode(homeService, homeDataStore, wiFiLiveData),
            ConnectToFaircon(app),
            DisconnectFromFaircon(app)
        )
    }
}