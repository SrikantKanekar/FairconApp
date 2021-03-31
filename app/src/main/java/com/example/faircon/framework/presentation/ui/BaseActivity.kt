package com.example.faircon.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.framework.datasource.connectivity.WiFiConnectivityManager
import com.example.faircon.framework.datasource.dataStore.SettingDataStore
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var wiFiConnectivityManager: WiFiConnectivityManager

    @Inject
    lateinit var settingDataStore: SettingDataStore

    val appTheme = mutableStateOf(Theme.DARK)

    override fun onStart() {
        super.onStart()
        wiFiConnectivityManager.registerWiFiObserver(this)

        lifecycleScope.launchWhenStarted {
            settingDataStore.settingFlow.collect { setting ->
                appTheme.value = setting.theme
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wiFiConnectivityManager.unregisterWiFiObserver(this)
    }
}