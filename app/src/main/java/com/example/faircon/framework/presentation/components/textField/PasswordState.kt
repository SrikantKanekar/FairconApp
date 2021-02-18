package com.example.faircon.framework.presentation.components.textField

class PasswordState(
    initialValue: String = ""
) : TextFieldState(
    initialValue = initialValue,
    validator = ::isPasswordValid,
    errorFor = ::passwordValidationError
)

class ConfirmPasswordState(private val passwordState: PasswordState) : TextFieldState() {
    override val isValid
        get() = passwordAndConfirmationValid(passwordState.text, text)

    override fun getError(): String? {
        return when {
            showErrors() -> passwordConfirmationError(passwordState.text, text)
            else -> null
        }
    }
}

private fun isPasswordValid(password: String): Boolean {
    return password.length > 3 && password.isNotBlank()
}

private fun passwordValidationError(password: String): String {
    return when {
        password.isBlank() -> "Password Cannot be empty"
        password.length <= 3 -> "Password must be at least 4 Characters"
        else -> ""
    }
}

private fun passwordAndConfirmationValid(password: String, confirmedPassword: String): Boolean {
    return isPasswordValid(password) && password == confirmedPassword
}

private fun passwordConfirmationError(password: String, confirmedPassword: String): String {
    return when {
        password.isBlank() -> "Password Cannot be empty"
        password.length <= 3 -> "Password must be at least 4 Characters"
        password != confirmedPassword -> "Passwords don't match"
        else -> ""
    }
}