package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.core.CorruptionException
import com.google.protobuf.InvalidProtocolBufferException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.HomePreferences
import com.example.faircon.business.domain.model.MODE
import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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

    private val default: HomePreferences = HomePreferences.newBuilder()
        .setFanSpeed(300)
        .setAmbientTemperature(15F)
        .setTecVoltage(0F)
        .setMode(MODE.ON)
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
                temperature = preferences.ambientTemperature,
                tecVoltage = preferences.tecVoltage,
                mode = preferences.mode
            )
        }

    suspend fun getParameter(): Parameter {
        val homePreference = try {
            dataStore.data.first()
        } catch (e: Exception) {
            e.printStackTrace()
            default
        }
        return Parameter(
            fanSpeed = homePreference.fanSpeed,
            temperature = homePreference.ambientTemperature,
            tecVoltage = homePreference.tecVoltage,
            mode = homePreference.mode
        )
    }

    suspend fun updateParameters(parameter: Parameter) {
        dataStore.updateData { homePreferences ->
            homePreferences.toBuilder()
                .setFanSpeed(parameter.fanSpeed)
                .setAmbientTemperature(parameter.temperature)
                .setTecVoltage(parameter.tecVoltage)
                .setMode(parameter.mode)
                .build()
        }
    }

    suspend fun updateMode(mode: Int) {
        dataStore.updateData { homePreferences ->
            homePreferences.toBuilder()
                .setMode(mode)
                .build()
        }
    }
}

object HomeSerializer : Serializer<HomePreferences> {
    override val defaultValue: HomePreferences =
        HomePreferences.newBuilder()
            .setFanSpeed(350)
            .setAmbientTemperature(25F)
            .setTecVoltage(10F)
            .setMode(MODE.ON)
            .build()


    override fun readFrom(input: InputStream): HomePreferences {
        try {
            return HomePreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override fun writeTo(t: HomePreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}