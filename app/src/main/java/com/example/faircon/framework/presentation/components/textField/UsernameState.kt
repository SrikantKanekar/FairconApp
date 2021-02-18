package com.example.faircon.framework.presentation.components.textField

class UsernameState(
    initialValue: String = ""
) : TextFieldState(
    initialValue = initialValue,
    validator = ::isUsernameValid,
    errorFor = ::usernameValidationError
)

private fun isUsernameValid(username: String): Boolean {
    return username.length <= 20 && username.isNotBlank()
}

private fun usernameValidationError(username: String): String {
    return when{
        username.length > 20 -> "Username cannot exceed 20 characters"
        else -> "Username cannot be empty"
    }
}
