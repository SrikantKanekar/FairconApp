package com.example.faircon.ui.auth.state

import com.example.faircon.util.StateEvent

sealed class AuthStateEvent: StateEvent {
    data class RegisterAttemptEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirm_password: String
    ): AuthStateEvent(){
        override fun errorInfo(): String {
            return "Register attempt failed."
        }
        override fun toString(): String {
            return "RegisterAttemptEvent"
        }
    }

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ): AuthStateEvent() {
        override fun errorInfo(): String {
            return "Login attempt failed."
        }
        override fun toString(): String {
            return "LoginAttemptEvent"
        }
    }

    object CheckPreviousAuthEvent : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error checking for previously authenticated user."
        }
        override fun toString(): String {
            return "CheckPreviousAuthEvent"
        }
    }

    object None : AuthStateEvent() {
        override fun errorInfo(): String {
            return "None"
        }
    }
}