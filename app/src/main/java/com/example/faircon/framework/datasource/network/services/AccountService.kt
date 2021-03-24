package com.example.faircon.framework.datasource.network.services

import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.example.faircon.framework.datasource.cache.entity.AccountProperties
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface AccountService {

    @GET("account/properties")
    suspend fun getAccountProperties(): AccountProperties

    @PUT("account/properties/update")
    @FormUrlEncoded
    suspend fun updateAccountProperties(
        @Field("email") email: String,
        @Field("username") username: String
    ): GenericResponse

    @PUT("account/change_password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): GenericResponse
}