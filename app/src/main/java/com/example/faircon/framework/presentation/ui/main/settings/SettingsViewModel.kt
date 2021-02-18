package com.example.faircon.framework.presentation.ui.main.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    var isDark by mutableStateOf(true)

    init {
        themeDataStore.preferenceFlow.onEach {
            isDark = it
        }.launchIn(CoroutineScope(Main))
    }

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeDataStore.updateAppTheme(isDark)
        }
    }
}