package com.example.faircon.presentation.ui.mode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.model.Mode.*
import com.example.faircon.presentation.components.ModeButton

@Composable
fun ModeScreen(
    viewModel: ModeViewModel,
    navigateToDiagnostics: () -> Unit,
    navigateToCooling: () -> Unit,
    navigateToHeating: () -> Unit,
    navigateToFan: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(15.dp)
                .clickable { navigateToDiagnostics() },
            imageVector = Icons.Default.Insights,
            contentDescription = "Diagnostics"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterVertically)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ModeButton(
                    selected = viewModel.currentMode == OFF,
                    mode = OFF,
                    imageVector = Icons.Default.PowerSettingsNew,
                    onClick = { viewModel.updateMode(it) },
                    navigate = { }
                )
                ModeButton(
                    selected = viewModel.currentMode == COOLING,
                    mode = COOLING,
                    imageVector = Icons.Default.AcUnit,
                    onClick = { viewModel.updateMode(it) },
                    navigate = {
                        if (viewModel.navigate) {
                            navigateToCooling()
                            viewModel.navigate = false
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                ModeButton(
                    selected = viewModel.currentMode == HEATING,
                    mode = HEATING,
                    imageVector = Icons.Default.LocalFireDepartment,
                    onClick = { viewModel.updateMode(it) },
                    navigate = {
                        if (viewModel.navigate) {
                            navigateToHeating()
                            viewModel.navigate = false
                        }
                    }
                )

                ModeButton(
                    selected = viewModel.currentMode == FAN,
                    mode = FAN,
                    imageVector = Icons.Default.Air,
                    onClick = { viewModel.updateMode(it) },
                    navigate = {
                        if (viewModel.navigate) {
                            navigateToFan()
                            viewModel.navigate = false
                        }
                    }
                )
            }
        }
    }
}