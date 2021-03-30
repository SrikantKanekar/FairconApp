package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent : StateEvent {

    object SyncControllerEvent : HomeStateEvent() {
        override fun errorInfo() = "Failed to Sync controller"
        override fun eventName() = "SyncControllerEvent"
        override fun shouldDisplayProgressBar() = false
    }

    class SetModeEvent(val mode: Mode) : HomeStateEvent() {
        override fun errorInfo() = "Failed to set FAIRCON mode"
        override fun eventName() = "SetModeEvent"
        override fun shouldDisplayProgressBar() = false
    }

    object ConnectToFairconEvent : HomeStateEvent() {
        override fun errorInfo() = "Faircon Connection error"
        override fun eventName() = "ConnectToFairconEvent"
        override fun shouldDisplayProgressBar() = true
    }

    object DisconnectFromFairconEvent : HomeStateEvent() {
        override fun errorInfo() = "Faircon disconnection error"
        override fun eventName() = "DisconnectFromFairconEvent"
        override fun shouldDisplayProgressBar() = true
    }
}