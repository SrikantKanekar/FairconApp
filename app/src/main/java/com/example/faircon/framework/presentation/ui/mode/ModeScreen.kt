package com.example.faircon.framework.presentation.ui.mode

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.model.Mode.*
import com.example.faircon.framework.presentation.theme.connect
import com.example.faircon.framework.presentation.theme.darkSurface

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
                ModeIcon(
                    selected = viewModel.currentMode == OFF,
                    mode = OFF,
                    imageVector = Icons.Default.PowerSettingsNew,
                    onClick = { viewModel.updateMode(it) },
                    navigate = { }
                )
                ModeIcon(
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

                ModeIcon(
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

                ModeIcon(
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

@Composable
fun ModeIcon(
    modifier: Modifier = Modifier,
    selected: Boolean,
    mode: Mode,
    imageVector: ImageVector,
    onClick: (Mode) -> Unit,
    navigate: () -> Unit
) {
    val transition = updateTransition(targetState = selected, label = null)

    if (selected && transition.currentState) navigate()

    val color by transition.animateColor(
        transitionSpec = { tween(1000) },
        label = "color"
    ) { state ->
        when (state) {
            true -> connect
            false -> darkSurface
        }
    }

    val tint by transition.animateColor(
        transitionSpec = { tween(1000) },
        label = "tint"
    ) { state ->
        when (state) {
            true -> Color.Black
            false -> Color.White
        }
    }

    Surface(
        modifier = modifier
            .size(100.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = { onClick(mode) }
            ),
        shape = CircleShape,
        color = color,
        elevation = 8.dp
    ) {
        Icon(
            modifier = Modifier.padding(30.dp),
            imageVector = imageVector,
            contentDescription = mode.name,
            tint = tint
        )
    }
}