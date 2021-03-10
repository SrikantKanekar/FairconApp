package com.example.faircon.framework.presentation.ui.main.controller.state

import com.example.faircon.business.domain.state.StateEvent

sealed class ControllerStateEvent : StateEvent {

    class SetFanSpeedEvent(val fanSpeed: Int) : ControllerStateEvent() {
        override fun errorInfo(): String {
            return "Failed to set Fan Speed"
        }

        override fun eventName(): String {
            return "SetFanSpeedEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    class SetTemperatureEvent(val temperature: Float) : ControllerStateEvent() {
        override fun errorInfo(): String {
            return "Failed to set Temperature"
        }

        override fun eventName(): String {
            return "SetTemperatureEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    class SetTecVoltageEvent(val voltage: Float) : ControllerStateEvent() {
        override fun errorInfo(): String {
            return "Failed to set Tec Voltage"
        }

        override fun eventName(): String {
            return "SetTecVoltageEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }
}