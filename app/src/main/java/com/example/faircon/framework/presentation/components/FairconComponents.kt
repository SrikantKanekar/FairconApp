package com.example.faircon.framework.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faircon.business.domain.model.IndicatorSize
import com.example.faircon.business.domain.model.IndicatorSize.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ControllerSlider(
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

fun Float.roundToHalf() = (this * 2).roundToInt().toFloat() / 2
fun Float.roundToOne() = (this * 10).roundToInt().toFloat() / 10

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
fun ParameterIndicator(
    modifier: Modifier = Modifier,
    name: String,
    unit: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    size: IndicatorSize
) {
    val sweepAngel = remember { Animatable(0f) }
    val currentValue = remember { Animatable(value.toInt(), Int.VectorConverter) }

    LaunchedEffect(Unit) {
        while (true) {
            val randomValue = Random.nextInt(valueRange.start.toInt(), valueRange.endInclusive.toInt())
            var percent =
                (randomValue - valueRange.start) / (valueRange.endInclusive - valueRange.start)
            if (percent > 1) percent = 1f else if (percent < 0) percent = 0f

            this.launch {
                currentValue.animateTo(
                    targetValue = randomValue,
                    animationSpec = tween(1500, easing = FastOutSlowInEasing)
                )
            }
            this.launch {
                sweepAngel.animateTo(
                    targetValue = 280 * percent,
                    animationSpec = tween(1500, easing = FastOutSlowInEasing)
                )
            }
            delay(1400)
        }
    }

    Box(modifier = modifier) {

        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(
                    when (size) {
                        SMALL -> 55.dp
                        MEDIUM -> 85.dp
                        LARGE -> 145.dp
                    }
                )
        ) {
            val stroke = Stroke(
                width = when (size) {
                    SMALL -> 6.dp.toPx()
                    MEDIUM -> 8.dp.toPx()
                    LARGE -> 13.dp.toPx()
                }
            )
            drawArc(
                color = Color.Red,
                startAngle = 130f,
                sweepAngle = sweepAngel.value,
                useCenter = false,
                style = stroke
            )

            drawArc(
                color = Color.Black,
                startAngle = 130f + sweepAngel.value,
                sweepAngle = 280f - sweepAngel.value,
                useCenter = false,
                style = stroke
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fontSize = when (size) {
                SMALL -> 10.sp
                MEDIUM -> 12.sp
                LARGE -> 28.sp
            }
            Text(
                text = "${currentValue.value} $unit",
                fontSize = fontSize,
                color = Color.White
            )
            if (size != LARGE){
                Text(
                    text = name,
                    fontSize = fontSize,
                    color = Color.White
                )
            }
        }
    }
}