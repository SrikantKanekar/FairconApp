package com.example.faircon.framework.presentation.ui.heating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.business.domain.model.IndicatorSize
import com.example.faircon.framework.datasource.network.webSocket.WebSocket
import com.example.faircon.framework.presentation.components.ControllerSlider
import com.example.faircon.framework.presentation.components.ParameterIndicator

@Composable
fun HeatingScreen(
    webSocket: WebSocket,
    navigateToDetails: () -> Unit
) {

    val faircon by webSocket.faircon.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { navigateToDetails() }
                ),
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 30.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ParameterIndicator(
                        name = "Room",
                        unit = "℃",
                        value = faircon.parameter.roomTemperature,
                        valueRange = 15F..25F,
                        size = IndicatorSize.LARGE
                    )
                    ParameterIndicator(
                        modifier = Modifier.padding(bottom = 6.dp),
                        name = "Power",
                        unit = "Kwh",
                        value = faircon.parameter.powerConsumption.toFloat(),
                        valueRange = 0F..1000F,
                        size = IndicatorSize.MEDIUM
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ParameterIndicator(
                        name = "Speed",
                        unit = "Rpm",
                        value = faircon.parameter.fanSpeed.toFloat(),
                        valueRange = 300F..400F,
                        size = IndicatorSize.SMALL
                    )
                    ParameterIndicator(
                        name = "Tec",
                        unit = "℃",
                        value = faircon.parameter.tecTemperature,
                        valueRange = 25F..120F,
                        size = IndicatorSize.SMALL
                    )
                    ParameterIndicator(
                        name = "Heat",
                        unit = "W",
                        value = faircon.parameter.heatExpelling.toFloat(),
                        valueRange = 0F..500F,
                        size = IndicatorSize.SMALL
                    )
                    ParameterIndicator(
                        name = "Voltage",
                        unit = "V",
                        value = faircon.parameter.tecVoltage,
                        valueRange = 0F..12F,
                        size = IndicatorSize.SMALL
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 30.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ControllerSlider(
                    name = "Fan Speed",
                    unit = "Rpm",
                    newValue = faircon.controller.fanSpeed.toFloat(),
                    valueRange = 300f..400f,
                    onValueChangeFinished = { webSocket.updateFanSpeed(it.toInt()) }
                )

                ControllerSlider(
                    name = "Temperature",
                    unit = "℃",
                    newValue = faircon.controller.temperature,
                    valueRange = 15f..25f,
                    onValueChangeFinished = { webSocket.updateTemperature(it) }
                )

                ControllerSlider(
                    name = "Tec Voltage",
                    unit = "V",
                    newValue = faircon.controller.tecVoltage,
                    valueRange = 0f..12f,
                    onValueChangeFinished = { webSocket.updateTecVoltage(it) }
                )
            }
        }
    }
}