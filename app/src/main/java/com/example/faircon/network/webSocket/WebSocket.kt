package com.example.faircon.network.webSocket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.faircon.model.Faircon
import com.example.faircon.model.Mode
import com.example.faircon.model.WebSocketEvent
import com.example.faircon.model.WebSocketMessage
import com.example.faircon.util.printLogD
import com.example.faircon.network.mappers.FairconMapper
import com.example.faircon.network.response.FairconResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * * A websocket connection between Faircon server and the app.
 * * Once initialized, it will contentiously try to connect to the server.
 * * After the connection is established, it will expose a [faircon] flow which will update in very short intervals.
 * * If the websocket is disconnected, it will automatically try to reconnect.
 */
@Singleton
class WebSocket
@Inject
constructor(
    private val webSocketService: WebSocketService,
    private val fairconMapper: FairconMapper
) {

    private val _faircon = MutableStateFlow(Faircon())
    val faircon: StateFlow<Faircon> = _faircon

    var isOpen by mutableStateOf(false)

    init {
        webSocketService.startSocket()
        CoroutineScope(Default).launch {
            webSocketService.subscribe().collect { socketUpdate ->
                handleSocketUpdate(socketUpdate)
            }
        }
    }

    private fun handleSocketUpdate(update: WebSocketEvent) {
        update.isOpen?.let { boolean ->
            isOpen = boolean
            printLogD("WebSocket", "isOpen : $boolean")
            if (!boolean) clearFaircon()
        }

        update.message?.let { message ->
            val fairconResponse = Gson().fromJson(message, FairconResponse::class.java)
            val faircon = fairconMapper.mapToDomainModel(fairconResponse)
            _faircon.value = faircon
            printLogD("WebSocket", "Raw : $message")
        }

        update.exception?.let { exception ->
            printLogD("WebSocket", "handleSocketUpdate exception : ${exception.message}")
            webSocketService.reconnectSocket()
        }
    }

    fun updateMode(mode: Mode) {
        val model = WebSocketMessage("mode", mode.ordinal)
        val message = Gson().toJson(model)
        sendMessage(message)
    }

    fun updateFanSpeed(value: Int) {
        val model = WebSocketMessage("fanSpeed", value)
        val message = Gson().toJson(model)
        sendMessage(message)
    }

    fun updateTemperature(value: Float) {
        val model = WebSocketMessage("temperature", value)
        val message = Gson().toJson(model)
        sendMessage(message)
    }

    fun updateTecVoltage(value: Float) {
        val model = WebSocketMessage("tecVoltage", value)
        val message = Gson().toJson(model)
        sendMessage(message)
    }

    private fun sendMessage(message: String) = webSocketService.sendMessage(message)

    private fun clearFaircon(){
        if (_faircon.value != Faircon()){
            _faircon.value = Faircon()
            printLogD("WebSocket", "clearFaircon : cleared")
        }
    }

    //fun closeSocket() = webSocketService.closeSocket()
}