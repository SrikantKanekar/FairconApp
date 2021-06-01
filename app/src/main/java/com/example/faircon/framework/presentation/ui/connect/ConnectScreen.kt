package com.example.faircon.framework.presentation.ui.connect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.ConnectButton

@Composable
fun ConnectScreen(
    viewModel: ConnectViewModel,
    navigateToMode: () -> Unit,
    navigateToSettings: () -> Unit
) {

    if (viewModel.initialCheck) {

        if (viewModel.navigateToModeScreen) {
            LaunchedEffect(Unit) { navigateToMode() }
        } else {

            Box(modifier = Modifier.fillMaxSize()) {

                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(15.dp)
                        .clickable { navigateToSettings() },
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )

                //To be removed
                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp)
                        .clickable { navigateToMode() },
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next"
                )

                ConnectButton(
                    modifier = Modifier.align(Alignment.Center),
                    connectionState = viewModel.connectionState,
                    onClick = { viewModel.connect() },
                    navigate = { navigateToMode() }
                )
            }
        }
    }
}