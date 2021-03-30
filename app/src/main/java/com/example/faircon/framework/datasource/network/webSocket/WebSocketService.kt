package com.example.faircon.framework.datasource.network.webSocket

import android.util.Log
import com.example.faircon.business.domain.model.Controller
import com.example.faircon.framework.di.WebSocketOkHttp
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class WebSocketService
@Inject
constructor(
    @WebSocketOkHttp private val okHttpClient: OkHttpClient,
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

    fun sendMessage() {
//        val model = Controller()
//        val message = Gson().toJson(model)
//        _webSocket?.send(message)
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
            Log.d("APP_DEBUG", "closeSocket exception: $e")
        }
    }
}