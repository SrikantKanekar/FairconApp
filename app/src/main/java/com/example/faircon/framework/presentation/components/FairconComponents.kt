package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.business.domain.model.Mode

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

@Composable
fun ShowParameter(
    name: String,
    unit: String,
    progress: Float,
    valueRange: ClosedFloatingPointRange<Float>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        MyOverlineText(text = name)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MyValueText(
                modifier = Modifier.width(80.dp),
                text = "$progress $unit"
            )

            MyLinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                progress = progress,
                valueRange = valueRange
            )
        }
    }
}

@Composable
fun ModeButtons(
    mode: Mode,
    setMode: (Mode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SelectableButton(
            isSelected = mode == Mode.OFF,
            text = "Idle",
            onClick = { setMode(Mode.OFF) }
        )

        SelectableButton(
            isSelected = mode == Mode.FAN,
            text = "Fan",
            onClick = { setMode(Mode.FAN) }
        )

        SelectableButton(
            isSelected = mode == Mode.COOLING,
            text = "Cooling",
            onClick = { setMode(Mode.COOLING) }
        )

        SelectableButton(
            isSelected = mode == Mode.HEATING,
            text = "Heating",
            onClick = { setMode(Mode.HEATING) }
        )
    }
}
