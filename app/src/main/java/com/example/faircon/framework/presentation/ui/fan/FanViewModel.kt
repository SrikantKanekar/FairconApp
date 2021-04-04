package com.example.faircon.framework.presentation.ui.fan

import androidx.lifecycle.ViewModel
import com.example.faircon.framework.datasource.network.webSocket.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FanViewModel @Inject constructor(
    private val webSocket: WebSocket
): ViewModel() {

    val faircon = webSocket.faircon

    fun updateFanSpeed(value: Int){
        webSocket.updateFanSpeed(value)
    }
}