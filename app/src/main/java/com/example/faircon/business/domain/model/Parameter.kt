package com.example.faircon.business.domain.model

import com.example.faircon.HomePreferences.Mode
import com.example.faircon.HomePreferences.Mode.IDLE
import com.example.faircon.HomePreferences.Status
import com.example.faircon.HomePreferences.Status.STABLE

data class Parameter(
    val fanSpeed: Int = 0,
    val roomTemperature: Float = 15F,
    val tecVoltage: Float = 0F,
    val powerConsumption: Int = 0,
    val heatExpelling: Int = 0,
    val tecTemperature: Float = 25F,
    val mode : Mode = IDLE,
    val status: Status = STABLE
)