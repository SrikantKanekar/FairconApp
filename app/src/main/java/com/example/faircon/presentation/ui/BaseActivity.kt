package com.example.faircon.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.dataStore.SettingDataStore
import com.example.faircon.network.webSocket.WebSocket
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var webSocket: WebSocket

    @Inject
    lateinit var settingDataStore: SettingDataStore

    var appTheme by mutableStateOf(Theme.DARK)

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenCreated {
            settingDataStore.settingFlow.collect { setting ->
                appTheme = setting.theme
            }
        }
    }
}