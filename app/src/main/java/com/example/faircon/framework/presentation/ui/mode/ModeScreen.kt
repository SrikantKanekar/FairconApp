package com.example.faircon.framework.presentation.ui.mode

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.SettingPreferences
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.theme.connect
import com.example.faircon.framework.presentation.theme.darkSurface

@Composable
fun ModeScreen(
    theme: SettingPreferences.Theme,
    scaffoldState: ScaffoldState,
    viewModel: ModeViewModel,
    navController: NavHostController
) {
    FairconTheme(
        theme = theme
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

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
                        selected = viewModel.currentMode == Mode.OFF,
                        mode = Mode.OFF,
                        imageVector = Icons.Default.PowerSettingsNew,
                        onClick = { viewModel.updateMode(it) },
                        navigate = { }
                    )
                    ModeIcon(
                        selected = viewModel.currentMode == Mode.COOLING,
                        mode = Mode.COOLING,
                        imageVector = Icons.Default.AcUnit,
                        onClick = { viewModel.updateMode(it) },
                        navigate = {
                            if (viewModel.navigate) {
                                navController.navigate(Cooling.route)
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
                        selected = viewModel.currentMode == Mode.HEATING,
                        mode = Mode.HEATING,
                        imageVector = Icons.Default.LocalFireDepartment,
                        onClick = { viewModel.updateMode(it) },
                        navigate = {
                            if (viewModel.navigate) {
                                navController.navigate(Heating.route)
                                viewModel.navigate = false
                            }
                        }
                    )

                    ModeIcon(
                        selected = viewModel.currentMode == Mode.FAN,
                        mode = Mode.FAN,
                        imageVector = Icons.Default.Air,
                        onClick = { viewModel.updateMode(it) },
                        navigate = {
                            if (viewModel.navigate) {
                                navController.navigate(Fan.route)
                                viewModel.navigate = false
                            }
                        }
                    )
                }
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