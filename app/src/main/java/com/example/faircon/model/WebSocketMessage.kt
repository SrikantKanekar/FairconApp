package com.example.faircon.model

data class WebSocketMessage<T>(
    val message: String,
    val value: T
)