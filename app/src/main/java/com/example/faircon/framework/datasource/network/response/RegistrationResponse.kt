package com.example.faircon.framework.datasource.network.response

import com.google.gson.annotations.SerializedName

class RegistrationResponse(
    var pk: Int,
    var email: String,
    var username: String,
    var token: String,
    var response: String,

    @SerializedName("error_message")
    var errorMessage: String
) {
    override fun toString(): String {
        return "RegistrationResponse(response='$response', errorMessage='$errorMessage', email='$email', username='$username', token='$token')"
    }
}