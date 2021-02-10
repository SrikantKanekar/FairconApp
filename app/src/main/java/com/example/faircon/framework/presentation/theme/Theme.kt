package com.example.faircon.framework.presentation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.faircon.framework.presentation.components.CircularIndeterminateProgressBar

private val LightColorPalette = lightColors(
    primary = blue300,
    primaryVariant = blue400,
    secondary = blue300,
    secondaryVariant = blue400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

private val DarkColorPalette = darkColors(
    primary = blue300,
    primaryVariant = blue400,
    secondary = blue300,
    background = Color.Black,
    surface = graySurface,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun FairconTheme(
    displayProgressBar: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = typography,
        shapes = shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar)
        }
    }
}