package com.example.faircon.business.interactors.connect

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.flow.flow

class ConnectToFaircon(
    private val app: BaseApplication
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow<Nothing> {

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
    }
}
