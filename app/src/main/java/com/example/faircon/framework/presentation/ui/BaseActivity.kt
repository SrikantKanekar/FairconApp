package com.example.faircon.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore
import com.example.faircon.framework.datasource.network.connectivity.WiFiConnectivityManager
import com.example.faircon.framework.datasource.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var wiFiConnectivityManager: WiFiConnectivityManager

    @Inject
    lateinit var themeDataStore: ThemeDataStore

    var isDark by mutableStateOf(true)

    override fun onStart() {
        super.onStart()
        observeTheme()
        wiFiConnectivityManager.registerWiFiObserver(this)
    }

    private fun observeTheme() {
        themeDataStore.preferenceFlow.onEach {
            isDark = it
        }.launchIn(CoroutineScope(Main))
    }

    override fun onDestroy() {
        super.onDestroy()
        wiFiConnectivityManager.unregisterWiFiObserver(this)
    }
}