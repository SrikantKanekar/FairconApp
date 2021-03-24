package com.example.faircon.framework.datasource.network.services

import com.example.faircon.business.domain.model.Controller
import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface ControllerService {

    @GET("controller/sync")
    suspend fun getController(): Controller

    @PUT("controller/fanSpeed")
    suspend fun setFanSpeed(
        @Body fanSpeed: FanSpeedRequest,
    ): GenericResponse

    @PUT("controller/temperature")
    suspend fun setTemperature(
        @Body temperature: TemperatureRequest,
    ): GenericResponse

    @PUT("controller/tecVoltage")
    suspend fun setTecVoltage(
        @Body tecVoltage: TecVoltageRequest,
    ): GenericResponse
}

class FanSpeedRequest(
    @SerializedName("fanSpeed")
    val fanSpeed: Int
)

class TemperatureRequest(
    @SerializedName("temperature")
    val temperature: Float
)

class TecVoltageRequest(
    @SerializedName("tecVoltage")
    val tecVoltage: Float
)