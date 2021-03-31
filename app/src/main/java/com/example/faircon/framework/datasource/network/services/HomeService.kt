package com.example.faircon.framework.datasource.network.services

import com.example.faircon.framework.datasource.network.response.GenericResponse
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface HomeService {

    @GET("test")
    suspend fun testConnection(): GenericResponse
}