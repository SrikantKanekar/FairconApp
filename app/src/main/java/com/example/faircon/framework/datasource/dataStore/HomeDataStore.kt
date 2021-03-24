package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.core.CorruptionException
import com.google.protobuf.InvalidProtocolBufferException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.HomePreferences
import com.example.faircon.HomePreferences.*
import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeDataStore
@Inject
constructor(
    app: BaseApplication
) {

    private val dataStore = app
        .createDataStore(DataStoreFiles.HOME_DATASTORE_FILE, HomeSerializer)

    private val default: HomePreferences = newBuilder()
        .setFanSpeed(0)
        .setRoomTemperature(15F)
        .setTecVoltage(0F)
        .setPowerConsumption(0)
        .setHeatExpelling(0)
        .setTecTemperature(25F)
        .setMode(Mode.IDLE)
        .setStatus(Status.STABLE)
        .build()

    val homeFlow: Flow<Parameter> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(default)
            } else {
                throw exception
            }
        }
        .map { preferences ->
            Parameter(
                fanSpeed = preferences.fanSpeed,
                roomTemperature = preferences.roomTemperature,
                tecVoltage = preferences.tecVoltage,
                powerConsumption = preferences.powerConsumption,
                heatExpelling = preferences.heatExpelling,
                tecTemperature = preferences.tecTemperature,
                mode = preferences.mode,
                status = preferences.status
            )
        }

    suspend fun updateParameters(parameter: Parameter) {
        dataStore.updateData { homePreferences ->
            homePreferences.toBuilder()
                .setFanSpeed(parameter.fanSpeed)
                .setRoomTemperature(parameter.roomTemperature)
                .setTecVoltage(parameter.tecVoltage)
                .setPowerConsumption(parameter.powerConsumption)
                .setHeatExpelling(parameter.heatExpelling)
                .setTecTemperature(parameter.tecTemperature)
                .setMode(parameter.mode)
                .setStatus(parameter.status)
                .build()
        }
    }

    suspend fun clear() {
        dataStore.updateData { homePreferences ->
            homePreferences.toBuilder()
                .setFanSpeed(0)
                .setRoomTemperature(15F)
                .setTecVoltage(0F)
                .setPowerConsumption(0)
                .setHeatExpelling(0)
                .setTecTemperature(25F)
                .setMode(Mode.IDLE)
                .setStatus(Status.STABLE)
                .build()
        }
    }

    suspend fun updateMode(mode: Mode) {
        dataStore.updateData { homePreferences ->
            homePreferences.toBuilder()
                .setMode(mode)
                .build()
        }
    }
}

object HomeSerializer : Serializer<HomePreferences> {
    override val defaultValue: HomePreferences =
        newBuilder()
            .setFanSpeed(0)
            .setRoomTemperature(15F)
            .setTecVoltage(0F)
            .setPowerConsumption(0)
            .setHeatExpelling(0)
            .setTecTemperature(25F)
            .setMode(Mode.IDLE)
            .setStatus(Status.STABLE)
            .build()


    override fun readFrom(input: InputStream): HomePreferences {
        try {
            return parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override fun writeTo(t: HomePreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}