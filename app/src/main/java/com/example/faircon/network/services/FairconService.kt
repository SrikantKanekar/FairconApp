package com.example.faircon.network.services

import com.example.faircon.network.response.GenericResponse
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface FairconService {

    @GET("test")
    suspend fun testConnection(): GenericResponse
}