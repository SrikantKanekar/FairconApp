package com.example.faircon.framework.datasource.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.createDataStore
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeDataStore
@Inject
constructor(
    app: BaseApplication
) {

    private val dataStore = app.createDataStore(DataStoreFiles.THEME_DATASTORE_FILE)

    val preferenceFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val theme: Boolean = preferences[PreferenceKeys.THEME] ?: THEME.DARK
            theme
        }

    suspend fun updateAppTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME] = isDark
        }
    }

    suspend fun toggleAppTheme() {
        dataStore.edit { preferences ->
            val value = preferences[PreferenceKeys.THEME] ?: THEME.DARK
            preferences[PreferenceKeys.THEME] = !value
        }
    }

    private object PreferenceKeys {
        val THEME = booleanPreferencesKey("APP_THEME")
    }

    object THEME{
        const val DARK = true
        const val LIGHT = false
    }
}