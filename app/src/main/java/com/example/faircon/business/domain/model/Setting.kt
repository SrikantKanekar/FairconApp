package com.example.faircon.business.domain.model

import com.example.faircon.SettingPreferences.Theme

data class Setting(
    val theme: Theme = Theme.DARK
)