package com.example.faircon.framework.datasource.network.webSocket

import android.util.Log
import com.example.faircon.business.domain.model.WebSocketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener : WebSocketListener() {

    private val _socketUpdate = MutableSharedFlow<WebSocketEvent>()
    val socketUpdate: SharedFlow<WebSocketEvent> = _socketUpdate

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("APP_DEBUG", "onOpen: called")
        webSocket.send("Hi from Faircon app")
        emit(WebSocketEvent(isConnected = true))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("APP_DEBUG", "onMessage: called")
        emit(WebSocketEvent(message = text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("APP_DEBUG", "onClosing: called")
        emit(WebSocketEvent(isConnected = false))
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("APP_DEBUG", "onClosed: called")
        emit(WebSocketEvent(isConnected = false))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("APP_DEBUG", "onFailure: called")
        emit(
            WebSocketEvent(
                isConnected = false,
                exception = t
            )
        )
    }

    private fun emit(update: WebSocketEvent) {
        CoroutineScope(IO).launch {
            _socketUpdate.emit(update)
        }
    }
}