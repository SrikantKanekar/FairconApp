package com.example.faircon.framework.presentation.ui.main.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.MyLinearProgressIndicator
import com.example.faircon.framework.presentation.components.MyOverlineText
import com.example.faircon.framework.presentation.components.MyValueText

@Composable
fun HomeScreen(
    viewModel : HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        ShowData(
            name = "Fan Speed",
            value = 350f,
            unit = "RPM",
            progress = 0.5f
        )

        ShowData(
            name = "Temperature",
            value = 19f,
            unit = "C",
            progress = 0.4f
        )

        ShowData(
            name = "Tec Voltage",
            value = 8.5f,
            unit = "V",
            progress = 0.7f
        )
    }
}

@Composable
fun ShowData(
    name: String,
    value: Float,
    unit: String,
    progress: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        MyOverlineText(text = name)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MyValueText(
                modifier = Modifier.width(80.dp),
                text = "$value $unit"
            )

            MyLinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                progress = progress
            )
        }
    }
}
