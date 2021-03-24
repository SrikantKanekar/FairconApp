package com.example.faircon.framework.presentation.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faircon.SettingPreferences
import com.example.faircon.SettingPreferences.*
import com.example.faircon.framework.datasource.dataStore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val settingDataStore: SettingDataStore
) : ViewModel() {

    val settingFlow = settingDataStore.settingFlow

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            settingDataStore.updateTheme(theme)
        }
    }
}