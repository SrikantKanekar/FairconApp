package com.example.faircon.framework.datasource.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.DARK
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.THEME_PREFERENCE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreferences
@Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()


    // App theme
    fun setTheme(theme: String){
        editor.putString(THEME_PREFERENCE, theme)
    }

    fun getTheme() : String? {
        return prefs.getString(THEME_PREFERENCE, DARK)
    }


    // Authenticated User Email
    fun setAuthenticatedUser(email: String) {
        editor.putString(AUTHENTICATED_USER, email)
        editor.apply()
    }

    fun getAuthenticatedUser(): String? {
        return prefs.getString(AUTHENTICATED_USER, null)
    }

    companion object {
        const val AUTHENTICATED_USER = "AUTHENTICATED_USER"
    }
}