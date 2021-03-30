package com.example.faircon.framework.datasource.network.services

import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import javax.inject.Singleton

@Singleton
interface HomeService {

    @PUT("home/mode")
    suspend fun setMode(
        @Body mode: ModeRequest,
    ): GenericResponse

    @GET("test")
    suspend fun testConnection(): GenericResponse
}

class ModeRequest(
    @SerializedName("mode")
    val mode: Int
)