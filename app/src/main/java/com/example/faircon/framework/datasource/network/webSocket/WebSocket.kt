package com.example.faircon.framework.datasource.network.webSocket

import com.example.faircon.business.domain.model.Faircon
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.model.WebSocketEvent
import com.example.faircon.business.domain.model.WebSocketMessage
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.datasource.network.mappers.FairconMapper
import com.example.faircon.framework.datasource.network.response.FairconResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocket
@Inject
constructor(
    private val webSocketService: WebSocketService,
    private val fairconMapper: FairconMapper
) {

    private val _faircon = MutableStateFlow(Faircon())
    val faircon: StateFlow<Faircon> = _faircon

    private val _isOpen = MutableStateFlow(false)
    val isOpen: StateFlow<Boolean> = _isOpen

    init {
        webSocketService.startSocket()
        CoroutineScope(Default).launch {
            webSocketService.subscribe().collect { socketUpdate ->
                handleSocketUpdate(socketUpdate)
            }
        }
    }

    private fun handleSocketUpdate(update: WebSocketEvent) {
        update.isConnected?.let { boolean ->
            _isOpen.value = boolean
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

    //fun closeSocket() = webSocketService.closeSocket()
}