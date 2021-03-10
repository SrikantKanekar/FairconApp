package com.example.faircon.framework.presentation.ui.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faircon.framework.presentation.components.MyIcon
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    isDark: Boolean,
    isWiFiAvailable: Boolean,
    scaffoldState: ScaffoldState
) {

    FairconTheme(
        isDark = isDark,
        isWiFiAvailable = isWiFiAvailable,
        scaffoldState = scaffoldState,
        stateMessage = null,
        removeStateMessage = {  }
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {

                SwitchSetting(
                    imageVector = Icons.Default.Person,
                    isDark = settingsViewModel.isDark,
                    value = if (settingsViewModel.isDark) "Dark" else "Light",
                    onCheckedChange = { boolean ->
                        settingsViewModel.setTheme(boolean)
                    }
                )
            }
        }
    }
}

@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    isDark: Boolean,
    value: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row{
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
            checked = isDark,
            onCheckedChange = onCheckedChange
        )
    }
}