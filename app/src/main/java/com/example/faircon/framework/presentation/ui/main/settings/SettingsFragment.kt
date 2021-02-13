package com.example.faircon.framework.presentation.ui.main.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.faircon.R
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore.*
import com.example.faircon.framework.datasource.dataStore.ThemeDataStore.Companion.APP_THEME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    @Inject
    lateinit var themeDataStore: ThemeDataStore

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)

        preferenceManager
            .findPreference<SwitchPreferenceCompat>(APP_THEME)
            ?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            APP_THEME -> {
                setTheme(newValue == THEME.DARK)
            }
        }
        return true
    }

    private fun setTheme(isDark: Boolean) {
        CoroutineScope(IO).launch {
            if (isDark) {
                themeDataStore.updateAppTheme(THEME.DARK)
            } else {
                themeDataStore.updateAppTheme(THEME.LIGHT)
            }
        }
    }
}