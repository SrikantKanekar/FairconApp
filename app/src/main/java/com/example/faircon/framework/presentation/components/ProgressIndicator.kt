package com.example.faircon.framework.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
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
    if (isDisplayed){
        CircularProgressIndicator(
            modifier = modifier
        )
    }
}