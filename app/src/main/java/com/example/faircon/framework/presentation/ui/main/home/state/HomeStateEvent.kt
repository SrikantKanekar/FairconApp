package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.state.StateEvent

sealed class HomeStateEvent :StateEvent{
    object StateEvent: HomeStateEvent() {
        override fun errorInfo(): String {
            TODO("Not yet implemented")
        }

        override fun eventName(): String {
            TODO("Not yet implemented")
        }

        override fun shouldDisplayProgressBar(): Boolean {
            TODO("Not yet implemented")
        }
    }
}