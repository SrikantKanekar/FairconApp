package com.example.faircon.business.domain.model

data class Parameter(
    val fanSpeed: Int,
    val temperature: Float,
    val tecVoltage: Float,
    val mode : Int
)

object MODE {
    const val ON = 0
    const val FAN = 1
    const val COOLING = 2
    const val HEATING = 3
}