package com.example.faircon.framework.presentation.ui.home.state

import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent : StateEvent {

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