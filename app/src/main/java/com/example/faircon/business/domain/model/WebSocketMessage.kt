package com.example.faircon.business.domain.model

data class WebSocketMessage<T>(
    val message: String,
    val value: T
)