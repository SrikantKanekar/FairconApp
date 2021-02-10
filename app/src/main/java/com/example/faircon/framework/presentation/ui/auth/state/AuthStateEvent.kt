package com.example.faircon.framework.presentation.ui.auth.state

import com.example.faircon.business.domain.state.StateEvent

sealed class AuthStateEvent : StateEvent {

    data class RegisterAttemptEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirm_password: String
    ) : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Register attempt failed."
        }

        override fun eventName(): String {
            return "RegisterAttemptEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ) : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Login attempt failed."
        }

        override fun eventName(): String {
            return "LoginAttemptEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    object CheckPreviousAuthEvent : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error checking for previously authenticated user."
        }

        override fun eventName(): String {
            return "CheckPreviousAuthEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    object None : AuthStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }

        override fun eventName(): String {
            return "None"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }
}