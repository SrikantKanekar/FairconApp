package com.example.faircon.framework.presentation.ui.fan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.framework.presentation.components.ShowParameter
import com.example.faircon.framework.presentation.components.ControllerSlider
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
                    name = "Power Consumption",
                    progress = faircon.parameter.powerConsumption.toFloat(),
                    unit = "Kwh",
                    valueRange = 0F..1000F
                )

                ControllerSlider(
                    name = "Fan Speed",
                    unit = "RPM",
                    newValue = faircon.controller.fanSpeed.toFloat(),
                    valueRange = 300f..400f,
                    onValueChangeFinished = { viewModel.updateFanSpeed(it.toInt()) }
                )
            }
        }
    }
}