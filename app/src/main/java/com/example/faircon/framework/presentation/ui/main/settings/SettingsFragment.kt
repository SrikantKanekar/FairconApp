package com.example.faircon.framework.presentation.ui.main.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.faircon.R
import com.example.faircon.framework.datasource.preference.ThemeManager
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.DARK
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.DEFAULT
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.LIGHT
import com.example.faircon.framework.datasource.preference.ThemeManager.Companion.THEME_PREFERENCE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)

        preferenceManager
            .findPreference<ListPreference>(THEME_PREFERENCE)
            ?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            THEME_PREFERENCE -> {
                setTheme(newValue.toString())
            }
        }
        return true
    }

    private fun setTheme(theme: String){
        when (theme) {
            DEFAULT -> {
                themeManager.setTheme(DEFAULT)
            }
            LIGHT -> {
                themeManager.setTheme(LIGHT)
            }
            DARK -> {
                themeManager.setTheme(DARK)
            }
        }
    }
}