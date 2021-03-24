package com.example.faircon.framework.datasource.network.services

import com.example.faircon.framework.datasource.network.response.LoginResponse
import com.example.faircon.framework.datasource.network.response.RegistrationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @POST("account/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ): RegistrationResponse

    @POST("account/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse
}
