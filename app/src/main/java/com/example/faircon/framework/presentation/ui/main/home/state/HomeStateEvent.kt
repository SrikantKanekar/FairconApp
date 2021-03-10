package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent : StateEvent {

    object GetParametersEvent : HomeStateEvent() {
        override fun errorInfo(): String {
            return "Failed to get Parameters"
        }

        override fun eventName(): String {
            return "GetParametersEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    object SyncControllerEvent : HomeStateEvent() {
        override fun errorInfo(): String {
            return "failed to Sync controller"
        }

        override fun eventName(): String {
            return "SyncControllerEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    class SetModeEvent(val mode: Int) : HomeStateEvent() {
        override fun errorInfo() = "Failed to set Fan mode"
        override fun eventName() = "SetModeEvent"
        override fun shouldDisplayProgressBar() = false
    }
}