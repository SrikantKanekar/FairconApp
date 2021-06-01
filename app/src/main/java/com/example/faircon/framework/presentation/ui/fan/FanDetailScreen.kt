package com.example.faircon.framework.presentation.ui.fan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.datasource.network.webSocket.WebSocket
import com.example.faircon.framework.presentation.components.LineChart

@Composable
fun FanDetailScreen(
    webSocket: WebSocket
) {

    val faircon by webSocket.faircon.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LineChart(
            name = "Fan Speed",
            unit = "Rpm",
            value = faircon.parameter.fanSpeed.toFloat(),
            valueRange = 300F..400F
        )

        LineChart(
            name = "Power Consumption",
            unit = "Kwh",
            value = faircon.parameter.powerConsumption.toFloat(),
            valueRange = 0F..1000F
        )

        LineChart(
            name = "Room Temperature",
            unit = "℃",
            value = faircon.parameter.roomTemperature,
            valueRange = 0f..25f
        )


        LineChart(
            name = "Tec Temperature",
            unit = "℃",
            value = faircon.parameter.tecTemperature,
            valueRange = 25F..120F
        )
    }
}