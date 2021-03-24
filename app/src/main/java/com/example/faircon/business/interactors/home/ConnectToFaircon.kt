package com.example.faircon.business.interactors.home

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.presentation.ui.BaseApplication
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.flow.flow

class ConnectToFaircon(
    private val app: BaseApplication
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow {

        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.apply {
            SSID = "\"FAIRCON\""
            preSharedKey = "\"12345678\""
        }
        val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.apply {
            isWifiEnabled = true
            val netId = addNetwork(wifiConfiguration)
            disconnect()
            enableNetwork(netId, true)
            reconnect()
        }
        emit(
            DataState.data(
                data = HomeViewState(connected = true),
                response = Response(
                    message = "Connected to Faircon",
                    uiType = UiType.None,
                    messageType = MessageType.Info
                ),
                stateEvent = stateEvent
            )
        )
    }
}
