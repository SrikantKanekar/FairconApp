package com.example.faircon.presentation.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.SettingPreferences.Theme.DARK
import com.example.faircon.SettingPreferences.Theme.LIGHT
import com.example.faircon.model.Setting

@Composable
fun SettingsScreen() {

    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settings = settingsViewModel.settingFlow.collectAsState(initial = Setting())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(top = 25.dp)
    ) {

        SwitchSetting(
            imageVector = Icons.Default.ColorLens,
            theme = settings.value.theme,
            value = if (settings.value.theme == DARK) "Dark" else "Light",
            onCheckedChange = { theme ->
                settingsViewModel.setTheme(theme)
            }
        )
    }
}

@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    theme: Theme,
    value: String,
    onCheckedChange: (Theme) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = modifier.requiredSize(20.dp),
                imageVector = imageVector,
                contentDescription = null
            )

            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text = "Theme",
                    fontSize = 15.sp
                )
                Text(
                    text = value,
                    fontSize = 13.sp
                )
            }
        }

        Switch(
            modifier = Modifier.padding(5.dp),
            checked = theme == DARK,
            onCheckedChange = { isDark ->
                onCheckedChange(if (isDark) DARK else LIGHT)
            }
        )
    }
}