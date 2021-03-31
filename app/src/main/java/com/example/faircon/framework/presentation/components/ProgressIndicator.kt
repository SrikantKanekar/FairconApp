package com.example.faircon.framework.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    valueRange: ClosedFloatingPointRange<Float>
) {
    val value = (progress - valueRange.start) / (valueRange.endInclusive - valueRange.start)

    val animatedProgress = animateFloatAsState(
        targetValue = if (value < 0) 0F else if (value > 1) 1F else value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LinearProgressIndicator(
        modifier = modifier,
        progress = animatedProgress.value
    )
}

@Composable
fun MyCircularProgressIndicator(
    isDisplayed: Boolean,
    modifier: Modifier = Modifier
) {
    if (isDisplayed) {
        CircularProgressIndicator(
            modifier = modifier
        )
    }
}