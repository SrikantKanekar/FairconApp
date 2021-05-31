package com.example.faircon.business.interactors.connect

import android.content.Context
import android.net.wifi.WifiManager
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.flow.flow

class DisconnectFromFaircon(
    private val app: BaseApplication
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow<Nothing> {

        val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
    }
}
