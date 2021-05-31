package com.example.faircon.framework.datasource.network.webSocket

import com.example.faircon.business.domain.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService
@Inject
constructor(
    private val okHttpClient: OkHttpClient,
    private val webSocketListener: WebSocketListener
) {

    private var _webSocket: WebSocket? = null

    fun subscribe() = webSocketListener.socketUpdate

    fun startSocket() {
        if (_webSocket == null) {
            _webSocket = okHttpClient.newWebSocket(
                Request.Builder().url("ws://192.168.4.1:81/").build(),
                webSocketListener
            )
        }
    }

    fun sendMessage(message: String) {
        _webSocket?.send(message)
    }

    fun reconnectSocket() {
        CoroutineScope(IO).launch {
            closeSocket()
            delay(500)
            startSocket()
        }
    }

    fun closeSocket() {
        try {
            _webSocket?.close(1000, null)
            _webSocket = null
        } catch (e: Exception) {
            printLogD("WebSocketService", "closeSocket exception: $e")
        }
    }
}