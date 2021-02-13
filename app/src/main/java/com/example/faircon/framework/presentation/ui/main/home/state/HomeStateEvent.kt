package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent :StateEvent{

    object GetParametersEvent: HomeStateEvent() {
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
}