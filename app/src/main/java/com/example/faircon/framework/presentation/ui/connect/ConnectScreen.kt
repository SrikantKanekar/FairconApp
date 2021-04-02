package com.example.faircon.framework.presentation.ui.connect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.framework.presentation.components.ConnectButton
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun ConnectScreen(
    theme: Theme,
    scaffoldState: ScaffoldState,
    viewModel: ConnectViewModel,
    navController: NavHostController
) {

    FairconTheme(
        theme = theme
    ) {

        if (viewModel.initialCheck) {

            if (viewModel.navigateToModeScreen) {
                LaunchedEffect(Unit) {
                    navController.navigate(Mode.route) {
                        popUpTo(Connect.route) { inclusive = true }
                    }
                }
            } else {

                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { scaffoldState.snackbarHostState },
                ) {

                    Box(modifier = Modifier.fillMaxSize()) {

                        Icon(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .clickable {
                                    navController.navigate(Settings.route)
                                },
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )

                        //To be removed
                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .clickable {
                                    navController.navigate(Mode.route) {
                                        popUpTo(Connect.route) { inclusive = true }
                                    }
                                },
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next"
                        )

                        ConnectButton(
                            modifier = Modifier.align(Alignment.Center),
                            connectionState = viewModel.connectionState,
                            onClick = { viewModel.connect() },
                            navigate = {
                                navController.navigate(Mode.route) {
                                    popUpTo(Connect.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}