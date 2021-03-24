package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.example.faircon.SettingPreferences
import com.example.faircon.SettingPreferences.*
import com.example.faircon.business.domain.model.Setting
import com.example.faircon.framework.presentation.ui.BaseApplication
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStore
@Inject
constructor(
    app: BaseApplication
) {

    private val dataStore = app
        .createDataStore(DataStoreFiles.SETTING_DATASTORE_FILE, SettingSerializer)

    private val default: SettingPreferences = newBuilder()
        .setTheme(Theme.DARK)
        .build()

    val settingFlow: Flow<Setting> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(default)
            } else {
                throw exception
            }
        }
        .map { preferences ->
            Setting(theme = preferences.theme)
        }

    suspend fun get(): Setting {
        val settingPreference = try {
            dataStore.data.first()
        } catch (e: Exception) {
            e.printStackTrace()
            default
        }
        return Setting(theme = settingPreference.theme)
    }

    suspend fun updateTheme(theme: Theme) {
        withContext(IO){
            dataStore.updateData { settingPreferences ->
                settingPreferences.toBuilder()
                    .setTheme(theme)
                    .build()
            }
        }
    }
}

object SettingSerializer : Serializer<SettingPreferences> {
    override val defaultValue: SettingPreferences =
        newBuilder()
            .setTheme(Theme.DARK)
            .build()


    override fun readFrom(input: InputStream): SettingPreferences {
        try {
            return parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }

    override fun writeTo(t: SettingPreferences, output: OutputStream) {
        return t.writeTo(output)
    }
}