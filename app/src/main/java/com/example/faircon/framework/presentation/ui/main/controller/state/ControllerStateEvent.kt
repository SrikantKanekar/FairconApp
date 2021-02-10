package com.example.faircon.framework.presentation.ui.main.controller.state

import com.example.faircon.business.domain.state.StateEvent

sealed class ControllerStateEvent : StateEvent {

    object GetSliderValueEvent : ControllerStateEvent() {
        override fun errorInfo(): String {
            return "failed to retrieve Values"
        }

        override fun eventName(): String {
            return "GetSliderValueEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    object SetFanSpeedEvent : ControllerStateEvent() {
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

    object SetTemperatureEvent : ControllerStateEvent() {
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

    object SetTecVoltageEvent : ControllerStateEvent() {
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