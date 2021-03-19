package com.example.faircon.business.domain.model

import com.example.faircon.SettingPreferences

data class Setting(
    val theme: Int = SettingPreferences.Theme.DEFAULT_VALUE
)