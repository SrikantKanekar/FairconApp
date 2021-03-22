package com.example.faircon.framework.presentation.ui.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.framework.presentation.components.MyIcon
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun SettingsScreen(
    theme: Int,
    isWiFiAvailable: Boolean,
    scaffoldState: ScaffoldState
) {

    FairconTheme(
        theme = theme,
        isWiFiAvailable = isWiFiAvailable,
        scaffoldState = scaffoldState,
        stateMessage = null,
        removeStateMessage = { }
    ) {

        val settingsViewModel = hiltNavGraphViewModel<SettingsViewModel>()
        val settings = settingsViewModel.settingFlow.collectAsState(initial = null)

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            if (settings.value != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {

                    SwitchSetting(
                        imageVector = Icons.Default.Person,
                        theme = settings.value!!.theme,
                        value = if (settings.value!!.theme == Theme.DARK_VALUE) "Dark" else "Light",
                        onCheckedChange = { theme ->
                            settingsViewModel.setTheme(theme)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    theme: Int,
    value: String,
    onCheckedChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            MyIcon(
                modifier = Modifier.padding(5.dp),
                imageVector = imageVector
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
            checked = theme == 1,
            onCheckedChange = { isDark ->
                onCheckedChange(if (isDark) 1 else 0)
            }
        )
    }
}