package com.example.faircon.framework.presentation.ui.controller

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.faircon.SettingPreferences.*
import com.example.faircon.framework.presentation.components.MyCircularProgressIndicator
import com.example.faircon.framework.presentation.components.MyOverlineText
import com.example.faircon.framework.presentation.components.MySlider
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun ControllerScreen(
    theme: Theme,
    isWiFiAvailable: Boolean,
    scaffoldState: ScaffoldState
) {

    val controllerViewModel = hiltNavGraphViewModel<ControllerViewModel>()
    val controller by controllerViewModel.controller.collectAsState(initial = null)

    FairconTheme(
        theme = theme,
        isWiFiAvailable = isWiFiAvailable,
        scaffoldState = scaffoldState
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {


            if (controller != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(modifier = Modifier.height(90.dp))

                    ShowSlider(
                        name = "Fan Speed",
                        initialPosition = controller!!.fanSpeed.toFloat(),
                        unit = "RPM",
                        valueRange = 300f..400f,
                        steps = 19,
                        onValueChangeFinished = { controllerViewModel.updateFanSpeed(it.toInt()) }
                    )

                    ShowSlider(
                        name = "Temperature",
                        initialPosition = controller!!.temperature,
                        unit = "C",
                        valueRange = 15f..25f,
                        steps = 9,
                        onValueChangeFinished = { controllerViewModel.updateTemperature(it) }
                    )

                    ShowSlider(
                        name = "Tec Voltage",
                        initialPosition = controller!!.tecVoltage,
                        unit = "V",
                        valueRange = 0f..12f,
                        steps = 23,
                        onValueChangeFinished = { controllerViewModel.updateTecVoltage(it) }
                    )
                }

            } else {
                MyCircularProgressIndicator(isDisplayed = true)
            }
        }
    }
}

@Composable
fun ShowSlider(
    name: String,
    initialPosition: Float,
    unit: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeFinished: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {

        var value by remember { mutableStateOf(initialPosition) }

        MyOverlineText(text = "$name : $value $unit")

        MySlider(
            initialPosition = initialPosition,
            valueRange = valueRange,
            steps = steps,
            onValueChange = { newValue ->
                value = newValue
            },
            onValueChangeFinished = {
                onValueChangeFinished(value)
            }
        )
    }
}
