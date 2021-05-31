package com.example.faircon.di

import com.example.faircon.business.interactors.connect.ConnectToFaircon
import com.example.faircon.business.interactors.connect.DisconnectFromFaircon
import com.example.faircon.business.interactors.connect.ConnectInteractors
import com.example.faircon.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideConnectInteractors(
        app: BaseApplication
    ): ConnectInteractors {
        return ConnectInteractors(
            ConnectToFaircon(app),
            DisconnectFromFaircon(app)
        )
    }
}