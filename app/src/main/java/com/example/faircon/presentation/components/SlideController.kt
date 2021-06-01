package com.example.faircon.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun SlideController(
    name: String,
    unit: String,
    newValue: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChangeFinished: (Float) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        var currentValue by remember { mutableStateOf(valueRange.start) }

        LaunchedEffect(newValue) {
            currentValue = newValue
        }

        val textValue = when(unit){
            "℃" -> currentValue.roundToHalf()
            "V" -> currentValue.roundToOne()
            else -> currentValue.roundToInt()
        }
        Text(
            text = "$name : $textValue $unit",
            fontSize = 14.sp,
            style = MaterialTheme.typography.overline
        )


        Slider(
            value = currentValue,
            onValueChange = { value ->
                currentValue = value
            },
            valueRange = valueRange,
            onValueChangeFinished = { onValueChangeFinished(currentValue) }
        )
    }
}