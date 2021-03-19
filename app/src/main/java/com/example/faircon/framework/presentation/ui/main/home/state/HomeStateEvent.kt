package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent : StateEvent {

    object GetParametersEvent : HomeStateEvent() {
        override fun errorInfo() = "Failed to get Parameters"
        override fun eventName() = "GetParametersEvent"
        override fun shouldDisplayProgressBar() = false
    }

    object SyncControllerEvent : HomeStateEvent() {
        override fun errorInfo() = "Failed to Sync controller"
        override fun eventName() = "SyncControllerEvent"
        override fun shouldDisplayProgressBar() = false
    }

    class SetModeEvent(val mode: Int) : HomeStateEvent() {
        override fun errorInfo() = "Failed to set FAIRCON mode"
        override fun eventName() = "SetModeEvent"
        override fun shouldDisplayProgressBar() = false
    }
}