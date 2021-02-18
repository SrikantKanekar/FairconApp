package com.example.faircon.framework.presentation.ui.main.settings.state

import com.example.faircon.business.domain.state.StateEvent

sealed class SettingsStateEvent: StateEvent {

    object GetSettingsData: SettingsStateEvent() {
        override fun errorInfo(): String {
            return ""
        }

        override fun eventName(): String {
            return "GetSettingsData"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    class SetTheme(val isDark: Boolean) : SettingsStateEvent() {
        override fun errorInfo(): String {
            return ""
        }

        override fun eventName(): String {
            return "SetTheme"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }
}