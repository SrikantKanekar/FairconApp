package com.example.faircon.framework.presentation.components.textField

import java.util.regex.Pattern

private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState(
    initialValue: String = ""
) : TextFieldState(
    initialValue = initialValue,
    validator = ::isEmailValid,
    errorFor = ::emailValidationError
)

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email) && email.isNotBlank()
}

private fun emailValidationError(email: String): String {
    return when {
        email.isBlank() -> "Email cannot be empty"
        else -> "Invalid email: $email"
    }
}
