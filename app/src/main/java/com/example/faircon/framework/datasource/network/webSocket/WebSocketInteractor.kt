package com.example.faircon.framework.datasource.network.webSocket

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketInteractor
@Inject
constructor(
    private val webSocketService: WebSocketService
) {
    fun startSocket() = webSocketService.startSocket()

    fun subscribe() = webSocketService.subscribe()

    fun sendMessage(message: String) = webSocketService.sendMessage(message)

    fun reconnectSocket() = webSocketService.reconnectSocket()

    fun closeSocket() = webSocketService.closeSocket()
}