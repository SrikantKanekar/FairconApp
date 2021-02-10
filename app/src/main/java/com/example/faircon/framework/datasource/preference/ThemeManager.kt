package com.example.faircon.framework.datasource.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager
@Inject
constructor(
    private val preferences: MyPreferences
) {

    private val _theme = MutableLiveData<String>(preferences.getTheme())

    val theme: LiveData<String>
        get() = _theme

    fun setTheme(theme: String){
        _theme.value = theme
        preferences.setTheme(theme)
    }

    companion object {
        const val THEME_PREFERENCE = "theme_preference"
        const val DEFAULT = "DEFAULT"
        const val LIGHT = "LIGHT"
        const val DARK = "DARK"
    }
}
