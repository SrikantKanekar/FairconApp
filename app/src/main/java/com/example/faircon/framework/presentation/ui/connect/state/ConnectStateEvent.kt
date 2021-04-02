package com.example.faircon.framework.presentation.ui.connect.state

import com.example.faircon.business.domain.state.StateEvent

sealed class ConnectStateEvent : StateEvent {

    object ConnectToFairconEvent : ConnectStateEvent() {
        override fun errorInfo() = "Faircon Connection error"
        override fun eventName() = "ConnectToFairconEvent"
        override fun shouldDisplayProgressBar() = true
    }

    object DisconnectFromFairconEvent : ConnectStateEvent() {
        override fun errorInfo() = "Faircon disconnection error"
        override fun eventName() = "DisconnectFromFairconEvent"
        override fun shouldDisplayProgressBar() = true
    }
}