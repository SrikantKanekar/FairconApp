package com.example.faircon.business.interactors.home

import com.example.faircon.framework.datasource.network.webSocket.WebSocketService
import javax.inject.Inject

class WebSocketInteractor
@Inject
constructor(
    private val webSocketService: WebSocketService
) {
    fun startSocket() = webSocketService.startSocket()

    fun subscribe() = webSocketService.subscribe()

    fun sendMessage() = webSocketService.sendMessage()

    fun reconnectSocket() = webSocketService.reconnectSocket()

    fun closeSocket() = webSocketService.closeSocket()
}