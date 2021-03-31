package com.example.faircon.framework.di

import com.example.faircon.business.interactors.home.ConnectToFaircon
import com.example.faircon.business.interactors.home.DisconnectFromFaircon
import com.example.faircon.business.interactors.home.HomeInteractors
import com.example.faircon.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

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