package com.example.faircon.framework.presentation.components

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun MySlider(
    modifier: Modifier = Modifier,
    initialPosition: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeEnd: (Float) -> Unit
) {

    val sliderPosition = remember { mutableStateOf(initialPosition) }

    Slider(
        modifier = modifier,
        value = sliderPosition.value,
        onValueChange = { sliderPosition.value = it },
        onValueChangeEnd = {
            onValueChangeEnd(sliderPosition.value)
        },
        valueRange = valueRange,
        steps = steps
    )
}