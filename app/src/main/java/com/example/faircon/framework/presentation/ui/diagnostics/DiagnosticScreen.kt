package com.example.faircon.framework.presentation.ui.diagnostics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.faircon.SettingPreferences
import com.example.faircon.framework.presentation.navigation.Navigation
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun DiagnosticScreen(
    theme: SettingPreferences.Theme,
    isWifiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {

    FairconTheme(
        theme = theme,
        scaffoldState = scaffoldState
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Navigation.Health.route) }
                        .padding(horizontal = 16.dp, vertical = 25.dp),
                    text = "Health"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Navigation.Performance.route) }
                        .padding(horizontal = 16.dp, vertical = 25.dp),
                    text = "Performance"
                )
            }
        }
    }
}