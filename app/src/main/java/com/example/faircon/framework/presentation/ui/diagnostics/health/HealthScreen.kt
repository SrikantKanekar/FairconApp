package com.example.faircon.framework.presentation.ui.diagnostics.health

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.faircon.SettingPreferences
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun HealthScreen(
    theme: SettingPreferences.Theme,
    scaffoldState: ScaffoldState
) {
    FairconTheme(
        theme = theme,
        scaffoldState = scaffoldState
    ) {

        val healthViewModel = hiltNavGraphViewModel<HealthViewModel>()

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Health"
                )
            }
        }
    }
}