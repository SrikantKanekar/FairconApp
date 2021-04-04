package com.example.faircon.framework.presentation.ui.cooling

import androidx.lifecycle.ViewModel
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.datasource.network.webSocket.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoolingViewModel @Inject constructor(
    private val webSocket: WebSocket
) : ViewModel() {

    val faircon = webSocket.faircon

    fun updateFanSpeed(value: Int) {
        webSocket.updateFanSpeed(value)
    }

    fun updateTemperature(value: Float) {
        webSocket.updateTemperature(value)
    }

    fun updateTecVoltage(value: Float) {
        webSocket.updateTecVoltage(value)
    }
}