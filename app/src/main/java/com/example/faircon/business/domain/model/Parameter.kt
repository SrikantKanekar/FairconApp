package com.example.faircon.business.domain.model

data class Parameter(
    val fanSpeed: Int = 300,
    val temperature: Float = 15F,
    val tecVoltage: Float = 0F,
    val mode : Int = 0
)

object MODE {
    const val ON = 0
    const val FAN = 1
    const val COOLING = 2
    const val HEATING = 3
}