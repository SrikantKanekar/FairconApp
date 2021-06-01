package com.example.faircon.network.response

data class FairconResponse(

    //Home
    val fanSpeed: Int = 0,
    val roomTemperature: Float = 15F,
    val tecVoltage: Float = 0F,
    val powerConsumption: Int = 0,
    val heatExpelling: Int = 0,
    val tecTemperature: Float = 25F,

    //Controller
    val requiredFanSpeed: Int = 0,
    val requiredTemperature: Float = 15F,
    val requitedTecVoltage: Float = 0F,

    //Mode
    val mode: Int = 0,

    //Status
    val status: Int = 0
)