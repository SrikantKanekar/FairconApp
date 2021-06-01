package com.example.faircon.model

data class WebSocketEvent(
    val isConnected: Boolean? = null,
    val message: String? = null,
    val exception: Throwable? = null
)