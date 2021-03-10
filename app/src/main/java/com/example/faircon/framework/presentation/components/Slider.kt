package com.example.faircon.framework.presentation.components

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MySlider(
    modifier: Modifier = Modifier,
    initialPosition: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
) {

    var position by remember { mutableStateOf(initialPosition) }

    Slider(
        modifier = modifier,
        value = position,
        valueRange = valueRange,
        steps = steps,
        onValueChange = { value ->
            position = value
            onValueChange(value)
        },
        onValueChangeFinished = onValueChangeFinished
    )
}