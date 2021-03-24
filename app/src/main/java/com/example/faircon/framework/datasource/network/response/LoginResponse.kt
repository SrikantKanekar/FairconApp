package com.example.faircon.framework.datasource.network.response

import com.google.gson.annotations.SerializedName

class LoginResponse(

    var pk: Int,
    var email: String,
    var token: String,
    var response: String,

    @SerializedName("error_message")
    var errorMessage: String
) {
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$errorMessage', token='$token', pk=$pk, email='$email')"
    }
}
