package com.example.faircon.presentation.ui.connect

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faircon.network.fairconConnection.FairconConnection
import com.example.faircon.presentation.ui.BaseApplication
import com.example.faircon.presentation.ui.connect.ConnectViewModel.ConnectionState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel
@Inject
constructor(
    private val fairconConnection: FairconConnection,
    private val application: BaseApplication
) : ViewModel() {

    /**
     * Waits for [fairconConnection] class to emit first non null value to determine weather the app is already connected to the Faircon sever.
     */
    var initialCheck by mutableStateOf(false)

    /**
     * navigate to mode screen if the app is already connected to Faircon Server
     */
    var navigateToModeScreen by mutableStateOf(false)

    var connectionState by mutableStateOf(CONNECT)

    init {
        viewModelScope.launch {
            fairconConnection.available.collect { connection ->
                connection?.let {

                    connectionState = when (connection) {
                        true -> CONNECTED
                        false -> CONNECT
                    }

                    // Executed only once, if connection is true -> navigated to mode screen
                    if (!initialCheck) {
                        navigateToModeScreen = connection
                        initialCheck = true
                    }
                }
            }
        }
    }

    fun connect() {
        if (connectionState == CONNECT || connectionState == RETRY) {
            connectionState = CONNECTING
            connectToFaircon()
            viewModelScope.launch {
                try {
                    withTimeout(5000) {
                        while (connectionState == CONNECTING) {
                            delay(100)
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    connectionState = RETRY
                    disconnectFromFaircon()
                }
            }
        }
    }

    private fun connectToFaircon() {
        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.apply {
            SSID = "\"FAIRCON\""
            preSharedKey = "\"12345678\""
        }
        val wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.apply {
            isWifiEnabled = true
            val netId = addNetwork(wifiConfiguration)
            disconnect()
            enableNetwork(netId, true)
            reconnect()
        }
    }

    private fun disconnectFromFaircon() {
        val wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
    }

    enum class ConnectionState {
        CONNECT, CONNECTING, CONNECTED, RETRY
    }
}