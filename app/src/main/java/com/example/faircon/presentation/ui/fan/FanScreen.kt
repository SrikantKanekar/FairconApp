package com.example.faircon.presentation.ui.fan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.model.IndicatorSize
import com.example.faircon.network.webSocket.WebSocket
import com.example.faircon.presentation.components.SlideController
import com.example.faircon.presentation.components.Indicator

@Composable
fun FanScreen(
    webSocket: WebSocket,
    navigateToDetails: () -> Unit
) {

    val faircon by webSocket.faircon.collectAsState()

    Column {

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
                    Indicator(
                        name = "Speed",
                        unit = "Rpm",
                        value = faircon.parameter.fanSpeed.toFloat(),
                        valueRange = 300F..400F,
                        size = IndicatorSize.LARGE
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Indicator(
                        modifier = Modifier.padding(bottom = 6.dp),
                        name = "Power",
                        unit = "Kwh",
                        value = faircon.parameter.powerConsumption.toFloat(),
                        valueRange = 0F..1000F,
                        size = IndicatorSize.SMALL
                    )

                    Indicator(
                        name = "Room",
                        unit = "℃",
                        value = faircon.parameter.roomTemperature,
                        valueRange = 15F..25F,
                        size = IndicatorSize.SMALL
                    )

                    Indicator(
                        name = "Tec",
                        unit = "℃",
                        value = faircon.parameter.tecTemperature,
                        valueRange = 25F..120F,
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
                modifier = Modifier.padding(16.dp)
            ) {
                SlideController(
                    name = "Fan Speed",
                    unit = "Rpm",
                    newValue = faircon.controller.fanSpeed.toFloat(),
                    valueRange = 300f..400f,
                    onValueChangeFinished = { webSocket.updateFanSpeed(it.toInt()) }
                )
            }
        }
    }
}