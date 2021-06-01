package com.example.faircon.framework.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
fun Indicator(
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
                },
                cap = StrokeCap.Round
            )
            drawArc(
                color = Color.Black,
                startAngle = 130f + sweepAngel.value,
                sweepAngle = 280f - sweepAngel.value,
                useCenter = false,
                style = stroke
            )

            drawArc(
                color = Color.Red,
                startAngle = 130f,
                sweepAngle = sweepAngel.value,
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

fun Float.roundToHalf() = (this * 2).roundToInt().toFloat() / 2
fun Float.roundToOne() = (this * 10).roundToInt().toFloat() / 10