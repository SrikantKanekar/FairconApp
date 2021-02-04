package com.example.faircon.framework.presentation.main.account.state

import com.example.faircon.business.domain.state.StateEvent

sealed class AccountStateEvent: StateEvent {

    object GetAccountPropertiesEvent : AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error retrieving account properties."
        }

        override fun eventName(): String {
            return "GetAccountPropertiesEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    data class UpdateAccountPropertiesEvent(
        val email: String,
        val username: String
    ): AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error updating account properties."
        }

        override fun eventName(): String {
            return "UpdateAccountPropertiesEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountStateEvent() {
        override fun errorInfo(): String {
            return "Error changing password."
        }

        override fun eventName(): String {
            return "ChangePasswordEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }

    object None : AccountStateEvent() {
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