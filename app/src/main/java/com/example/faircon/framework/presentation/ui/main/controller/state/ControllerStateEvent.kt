package com.example.faircon.framework.presentation.ui.main.controller.state

import com.example.faircon.business.domain.state.StateEvent

sealed class ControllerStateEvent : StateEvent {

    class SetFanSpeedEvent(val fanSpeed: Int) : ControllerStateEvent() {
        override fun errorInfo() = "Failed to set Fan Speed"
        override fun eventName() = "SetFanSpeedEvent"
        override fun shouldDisplayProgressBar() = false
    }

    class SetTemperatureEvent(val temperature: Float) : ControllerStateEvent() {
        override fun errorInfo() = "Failed to set Temperature"
        override fun eventName() = "SetTemperatureEvent"
        override fun shouldDisplayProgressBar() = false
    }

    class SetTecVoltageEvent(val voltage: Float) : ControllerStateEvent() {
        override fun errorInfo() = "Failed to set Tec Voltage"
        override fun eventName() = "SetTecVoltageEvent"
        override fun shouldDisplayProgressBar() = false
    }
}