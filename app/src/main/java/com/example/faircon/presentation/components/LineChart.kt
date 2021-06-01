package com.example.faircon.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    name: String,
    unit: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    lineColors: List<Color> = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.primary),
) {
    var currentValue by remember{ mutableStateOf(0f) }

    val textValue = when(unit){
        "℃" -> currentValue.roundToHalf()
        "V" -> currentValue.roundToOne()
        else -> currentValue.roundToInt()
    }

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(11.dp),
            text = "$name : $textValue $unit",
            fontSize = 14.sp
        )

        Card(shape = MaterialTheme.shapes.large) {
            Chart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = value,
                valueRange = valueRange,
                lineColors = lineColors,
                onValueUpdated = { currentValue = it }
            )
        }
    }
}

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    lineColors: List<Color>,
    onValueUpdated: (Float) -> Unit
) {

    val s = valueRange.start
    var yValues by remember {
        mutableStateOf(
            mutableListOf(
                s, s, s, s, s, s, s, s, s, s,
                s, s, s, s, s, s, s, s, s, s
            )
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val list = ArrayList(yValues)
            list.removeFirst()
            list.add(
                Random.nextDouble(
                    valueRange.start.toDouble(),
                    valueRange.endInclusive.toDouble()
                ).toFloat()
            )
            yValues = list
            onValueUpdated(yValues.last())
        }
    }

    val path = Path()
    val xbounds = Pair(0f, 19f)
    val ybounds = Pair(valueRange.start, valueRange.endInclusive)

    Canvas(
        modifier = modifier.padding(8.dp)
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

        drawPath(
            path = path,
            brush = Brush.linearGradient(lineColors),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}