package com.example.faircon.framework.presentation.ui.auth.state

import com.example.faircon.framework.datasource.cache.authToken.AuthToken

data class AuthViewState(
    var loginFields: LoginFields = LoginFields(),

    var registrationFields: RegistrationFields = RegistrationFields(),

    var authToken: AuthToken? = null
)

data class LoginFields(
    var login_email: String = "",
    var login_password: String = ""
) {
    class LoginError {

        companion object {
            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValidForLogin(): String {

        if (login_email.isEmpty()
            || login_password.isEmpty()
        ) {
            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$login_email, password=$login_password)"
    }
}

data class RegistrationFields(
    var registration_email: String = "",
    var registration_username: String = "",
    var registration_password: String = "",
    var registration_confirm_password: String = ""
) {

    class RegistrationError {
        companion object {
            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValidForRegistration(): String {
        if (registration_email.isEmpty()
            || registration_username.isEmpty()
            || registration_password.isEmpty()
            || registration_confirm_password.isEmpty()
        ) {
            return RegistrationError.mustFillAllFields()
        }

        if (registration_password != registration_confirm_password) {
            return RegistrationError.passwordsDoNotMatch()
        }
        return RegistrationError.none()
    }
}
