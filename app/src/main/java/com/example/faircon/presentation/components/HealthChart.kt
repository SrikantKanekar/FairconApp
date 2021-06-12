package com.example.faircon.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.ArrayList
import kotlin.random.Random

@Composable
fun HealthChart(
    modifier: Modifier = Modifier,
    yValues: List<Int>,
    lineColors: List<Color> = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.primary)
) {

    val path = Path()
    val xbounds = Pair(0f, 19f)
    val ybounds = Pair(0f, 100f)

    val coordinates = Path()

    Canvas(
        modifier = modifier
    ) {
        val scaleX = size.width / (xbounds.second - xbounds.first)
        val scaleY = size.height / (ybounds.second - ybounds.first)
        val yMove = ybounds.first * scaleY

        (0 until yValues.size).forEach { value ->
            val x = value * scaleX
            val y = size.height - (yValues[value] * scaleY) + yMove
            if (value == 0) {
                path.moveTo(0f, y)
                return@forEach
            }
            path.lineTo(x, y)
        }

        coordinates.moveTo(0f, 0f)
        coordinates.lineTo(0f, size.height)
        coordinates.lineTo(size.width, size.height)

        drawPath(
            path = path,
            brush = Brush.linearGradient(lineColors),
            style = Stroke(width = 2.dp.toPx())
        )

        drawPath(
            path = coordinates,
            color = Color.Gray,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}