package com.example.faircon.framework.presentation.ui.mode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.framework.datasource.network.webSocket.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeViewModel@Inject constructor(
    private val webSocket: WebSocket
) : ViewModel() {

    var currentMode by mutableStateOf(Mode.OFF)
    var navigate by mutableStateOf(false)

    fun updateMode(mode: Mode) {
        webSocket.updateMode(mode)
        currentMode = mode
        navigate = true
    }
}