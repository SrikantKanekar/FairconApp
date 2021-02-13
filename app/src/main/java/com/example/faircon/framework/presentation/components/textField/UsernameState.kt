package com.example.faircon.framework.presentation.components.textField

class UsernameState(
    initialValue: String = ""
) : TextFieldState(
    initialValue = initialValue,
    validator = ::isUsernameValid,
    errorFor = ::usernameValidationError
)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun usernameValidationError(username: String): String {
    return "Username cannot exceed 30 characters"
}

private fun isUsernameValid(username: String): Boolean {
    return username.length <= 30
}