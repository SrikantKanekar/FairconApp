package com.example.faircon.framework.presentation.ui.cooling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.SettingPreferences
import com.example.faircon.framework.presentation.components.ModeButtons
import com.example.faircon.framework.presentation.components.ShowParameter
import com.example.faircon.framework.presentation.components.ShowSlider
import com.example.faircon.framework.presentation.theme.FairconTheme

@Composable
fun CoolingScreen(
    theme: SettingPreferences.Theme,
    isWifiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    viewModel: CoolingViewModel
) {
    FairconTheme(
        theme = theme,
        isWifiAvailable = isWifiAvailable
    ) {

        val faircon by viewModel.faircon.collectAsState()

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(40.dp),
                    text = faircon.status.name
                )

                ShowParameter(
                    name = "Fan Speed",
                    unit = "RPM",
                    progress = faircon.parameter.fanSpeed.toFloat(),
                    valueRange = 300F..400F
                )

                ShowParameter(
                    name = "Room Temperature",
                    progress = faircon.parameter.roomTemperature,
                    unit = "C",
                    valueRange = 15F..25F
                )

                ShowParameter(
                    name = "Tec Voltage",
                    progress = faircon.parameter.tecVoltage,
                    unit = "V",
                    valueRange = 0F..12F
                )

                ShowParameter(
                    name = "Power Consumption",
                    progress = faircon.parameter.powerConsumption.toFloat(),
                    unit = "Kwh",
                    valueRange = 0F..1000F
                )

                ShowParameter(
                    name = "Heat Expelling",
                    progress = faircon.parameter.heatExpelling.toFloat(),
                    unit = "W",
                    valueRange = 0F..500F
                )

                ShowParameter(
                    name = "Tec Temperature",
                    progress = faircon.parameter.tecTemperature,
                    unit = "C",
                    valueRange = 25F..120F
                )

                ModeButtons(
                    mode = faircon.mode,
                    setMode = { viewModel.updateMode(it) }
                )

                ShowSlider(
                    name = "Fan Speed",
                    initialPosition = faircon.controller.fanSpeed.toFloat(),
                    unit = "RPM",
                    valueRange = 300f..400f,
                    steps = 19,
                    onValueChangeFinished = { viewModel.updateFanSpeed(it.toInt()) }
                )

                ShowSlider(
                    name = "Temperature",
                    initialPosition = faircon.controller.temperature,
                    unit = "C",
                    valueRange = 15f..25f,
                    steps = 9,
                    onValueChangeFinished = { viewModel.updateTemperature(it) }
                )

                ShowSlider(
                    name = "Tec Voltage",
                    initialPosition = faircon.controller.tecVoltage,
                    unit = "V",
                    valueRange = 0f..12f,
                    steps = 23,
                    onValueChangeFinished = { viewModel.updateTecVoltage(it) }
                )
            }
        }
    }
}