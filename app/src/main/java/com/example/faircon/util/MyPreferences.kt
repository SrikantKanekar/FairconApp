package com.example.faircon.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreferences
@Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveAuthenticatedUser(email: String) {
        val editor = prefs.edit()
        editor.putString("AuthenticatedUser", email)
        editor.apply()
    }

    fun getAuthenticatedUser(): String? {
        return prefs.getString("AuthenticatedUser", null)
    }
}