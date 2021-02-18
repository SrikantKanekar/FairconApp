package com.example.faircon.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore
import com.example.faircon.framework.datasource.network.connectivity.ConnectivityManager
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
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var dataStore: ThemeDataStore

    val isDark = mutableStateOf(true)

    override fun onStart() {
        super.onStart()
        observeTheme()
        connectivityManager.registerConnectionObserver(this)
    }

    private fun observeTheme() {
        dataStore.preferenceFlow.onEach {
            isDark.value = it
        }.launchIn(CoroutineScope(Main))
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }
}