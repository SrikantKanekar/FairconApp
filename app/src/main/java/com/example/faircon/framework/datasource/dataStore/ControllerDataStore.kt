package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.ControllerPreferences
import com.example.faircon.business.domain.model.Controller
import com.example.faircon.framework.presentation.ui.BaseApplication
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    private val default: ControllerPreferences = ControllerPreferences.newBuilder()
        .setFanSpeed(300)
        .setRequiredTemperature(15F)
        .setTecVoltage(0F)
        .build()

    val controllerFlow: Flow<Controller> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(default)
            } else {
                throw exception
            }
        }
        .map {
            Controller(it.fanSpeed, it.requiredTemperature, it.tecVoltage)
        }

    suspend fun getController(): Controller {
        val controllerPreference = try {
            dataStore.data.first()
        } catch (e: Exception) {
            e.printStackTrace()
            default
        }
        return Controller(
            fanSpeed = controllerPreference.fanSpeed,
            temperature = controllerPreference.requiredTemperature,
            tecVoltage = controllerPreference.tecVoltage
        )
    }

    fun updateController(controller: Controller) {
        CoroutineScope(IO).launch {
            dataStore.updateData { controllerPreferences ->
                controllerPreferences.toBuilder()
                    .setFanSpeed(controller.fanSpeed)
                    .setRequiredTemperature(controller.temperature)
                    .setTecVoltage(controller.tecVoltage)
                    .build()
            }
        }
    }
}

object ControllerSerializer : Serializer<ControllerPreferences> {
    override val defaultValue: ControllerPreferences =
        ControllerPreferences.newBuilder()
            .setFanSpeed(300)
            .setRequiredTemperature(15F)
            .setTecVoltage(0F)
            .build()

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