package com.example.faircon.framework.presentation.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var dataStore: ThemeDataStore

    val isDark = mutableStateOf(false)

    override fun onCreate() {
        super.onCreate()
        observeTheme()
    }

    private fun observeTheme() {
        dataStore.preferenceFlow.onEach {
            isDark.value = it
        }
    }
}