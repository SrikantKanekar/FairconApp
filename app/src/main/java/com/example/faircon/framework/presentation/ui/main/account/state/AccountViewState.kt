package com.example.faircon.framework.presentation.ui.main.account.state

import com.example.faircon.framework.datasource.cache.main.model.AccountProperties

class AccountViewState(
    var accountProperties: AccountProperties? = null,

    var updateAccountFields: UpdateAccountField = UpdateAccountField(),

    var changePasswordFields: ChangePasswordField = ChangePasswordField()
){
    data class UpdateAccountField(
        var email: String = "",
        var username: String = ""
    )

    data class ChangePasswordField(
        var currentPassword: String = "",
        var newPassword: String = "",
        var confirmPassword: String = ""
    )
}