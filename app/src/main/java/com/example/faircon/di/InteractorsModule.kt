package com.example.faircon.di

import com.example.faircon.business.interactors.connect.ConnectToFaircon
import com.example.faircon.business.interactors.connect.DisconnectFromFaircon
import com.example.faircon.business.interactors.connect.ConnectInteractors
import com.example.faircon.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

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