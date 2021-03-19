package com.example.faircon.framework.presentation.ui.account.state

import com.example.faircon.business.domain.state.StateEvent

sealed class AccountStateEvent : StateEvent {

    object GetAccountPropertiesEvent : AccountStateEvent() {
        override fun errorInfo() = "Error retrieving account Info"
        override fun eventName() = "GetAccountPropertiesEvent"
        override fun shouldDisplayProgressBar() = true
    }

    data class UpdateAccountPropertiesEvent(
        val email: String,
        val username: String
    ) : AccountStateEvent() {
        override fun errorInfo() = "Error updating account properties"
        override fun eventName() = "UpdateAccountPropertiesEvent"
        override fun shouldDisplayProgressBar() = true
    }

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ) : AccountStateEvent() {
        override fun errorInfo() = "Error changing password"
        override fun eventName() = "ChangePasswordEvent"
        override fun shouldDisplayProgressBar() = true
    }
}