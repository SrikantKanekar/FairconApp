package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.core.CorruptionException
import com.google.protobuf.InvalidProtocolBufferException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.ControllerPreferences
import com.example.faircon.business.domain.model.Controller
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
class ControllerDataStore
@Inject
constructor(
    app: BaseApplication
) {

    private val dataStore = app
        .createDataStore(DataStoreFiles.CONTROLLER_DATASTORE_FILE, ControllerSerializer)

    val controllerFlow: Flow<Controller> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(ControllerPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map{
            Controller(it.fanSpeed, it.requiredTemperature, it.tecVoltage)
        }

    suspend fun getController(): Controller {
        val controllerPreference = try {
            dataStore.data.first()
        } catch (e: Exception) {
            e.printStackTrace()
            ControllerPreferences.getDefaultInstance()
        }
        return Controller(
            fanSpeed = controllerPreference.fanSpeed,
            temperature = controllerPreference.requiredTemperature,
            tevVoltage = controllerPreference.tecVoltage
        )
    }

    suspend fun updateController(controller: Controller) {
        dataStore.updateData { controllerPreferences ->
            controllerPreferences.toBuilder()
                .setFanSpeed(controller.fanSpeed)
                .setRequiredTemperature(controller.temperature)
                .setTecVoltage(controller.tevVoltage)
                .build()
        }
    }

    suspend fun updateFanSpeed(fanSpeed: Int) {
        dataStore.updateData { controllerPreferences ->
            controllerPreferences.toBuilder()
                .setFanSpeed(fanSpeed)
                .build()
        }
    }

    suspend fun updateTemperature(temperature: Float) {
        dataStore.updateData { controllerPreferences ->
            controllerPreferences.toBuilder()
                .setRequiredTemperature(temperature)
                .build()
        }
    }

    suspend fun updateTecVoltage(voltage: Float) {
        dataStore.updateData { controllerPreferences ->
            controllerPreferences.toBuilder()
                .setTecVoltage(voltage)
                .build()
        }
    }
}


object ControllerSerializer : Serializer<ControllerPreferences> {
    override val defaultValue: ControllerPreferences
        get() = ControllerPreferences.getDefaultInstance()

    override fun readFrom(input: InputStream): ControllerPreferences {
        try {
            return ControllerPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override fun writeTo(t: ControllerPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}