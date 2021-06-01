package com.example.faircon.presentation.ui.cooling

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
import com.example.faircon.network.webSocket.WebSocket
import com.example.faircon.presentation.components.LineChart

@Composable
fun CoolingDetailScreen(
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
            name = "Room Temperature",
            unit = "℃",
            value = faircon.parameter.roomTemperature,
            valueRange = 0f..25f
        )

        LineChart(
            name = "Power Consumption",
            unit = "Kwh",
            value = faircon.parameter.powerConsumption.toFloat(),
            valueRange = 0F..1000F
        )

        LineChart(
            name = "Fan Speed",
            unit = "Rpm",
            value = faircon.parameter.fanSpeed.toFloat(),
            valueRange = 300F..400F
        )

        LineChart(
            name = "Tec Temperature",
            unit = "℃",
            value = faircon.parameter.tecTemperature,
            valueRange = 25F..120F
        )

        LineChart(
            name = "Heat Expelling",
            unit = "W",
            value = faircon.parameter.heatExpelling.toFloat(),
            valueRange = 0F..500F
        )

        LineChart(
            name = "Tec Voltage",
            unit = "V",
            value = faircon.parameter.tecVoltage,
            valueRange = 0F..12F
        )
    }
}
