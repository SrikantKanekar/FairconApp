package com.example.faircon.business.domain.model

data class Parameter(
    val fanSpeed: Int = 0,
    val roomTemperature: Float = 15F,
    val tecVoltage: Float = 0F,
    val powerConsumption: Int = 0,
    val heatExpelling: Int = 0,
    val tecTemperature: Float = 25F
)