package com.example.faircon.framework.presentation.ui.fan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun FanScreen(
    theme: Theme,
    isWifiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    viewModel: FanViewModel
) {
    FairconTheme(
        theme = theme,
        isWifiAvailable = isWifiAvailable
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Fan"
                )
            }
        }
    }
}