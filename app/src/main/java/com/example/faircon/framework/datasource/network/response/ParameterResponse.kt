package com.example.faircon.framework.datasource.network.response

data class ParameterResponse(
    val fanSpeed: Int = 0,
    val temperature: Float = 15F,
    val tecVoltage: Float = 0F,
    val powerConsumption: Int = 0,
    val heatExpelling: Int = 0,
    val tecTemperature: Float = 25F,
    val mode : Int = 0,
    val status: Int = 0
)