package com.example.faircon.presentation.ui.mode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.faircon.model.Mode
import com.example.faircon.network.webSocket.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeViewModel
@Inject
constructor(
    private val webSocket: WebSocket
) : ViewModel() {

    var currentMode by mutableStateOf(webSocket.faircon.value.mode)
    /**
     * flag to allow mode buttons to navigate to their destination
     */
    var navigate by mutableStateOf(false)

    fun updateMode(mode: Mode) {
        currentMode = mode
        webSocket.updateMode(mode)
        navigate = true
    }
}