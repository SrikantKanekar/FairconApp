package com.example.faircon.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faircon.presentation.theme.connect
import com.example.faircon.presentation.theme.connected
import com.example.faircon.presentation.theme.connecting
import com.example.faircon.presentation.ui.diagnostics.performance.PerformanceTestStates
import com.example.faircon.presentation.ui.diagnostics.performance.PerformanceTestStates.*

@Composable
fun PerformanceTestButton(
    modifier: Modifier = Modifier,
    currentState: PerformanceTestStates,
    startTest: () -> Unit
) {
    val transition = updateTransition(targetState = currentState, label = null)

    val color by transition.animateColor(
        transitionSpec = { tween(1500) },
        label = "ColorAnimation"
    ) { state ->
        when (state) {
            NONE -> connect
            STARTING -> connecting
            MOTOR_TEST -> connecting
            TEC_TEST -> connecting
            OTHER_TEST -> connecting
            EVALUATING -> connected
            COMPLETED -> connected
        }
    }

    val textColor by transition.animateColor(
        transitionSpec = { tween(1500) },
        label = "TextColorAnimation"
    ) { state ->
        when (state) {
            NONE -> Color.Black
            STARTING -> Color.Black
            MOTOR_TEST -> Color.Black
            TEC_TEST -> Color.Black
            OTHER_TEST -> Color.Black
            EVALUATING -> Color.Black
            COMPLETED -> Color.Black
        }
    }

    val loadingTransition = rememberInfiniteTransition()

    val animatedFloat by loadingTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart)
    )

    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { startTest() }
                )
        ) {
            drawCircle(
                color = color.copy(alpha = 0.8F),
                radius = 98.dp.toPx()
            )
            drawCircle(
                color = color.copy(alpha = 0.4F),
                radius = 80.dp.toPx()
            )

            if (currentState == MOTOR_TEST ||
                currentState == TEC_TEST ||
                currentState == OTHER_TEST
            ) {
                val stroke = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
                val diameter = size.minDimension
                val radius = diameter / 2f
                val insideRadius = radius - stroke.width
                val topLeftOffset = Offset(10f, 10f)
                val size = Size(insideRadius * 2, insideRadius * 2)
                var rotationAngle = animatedFloat - 50f
                drawArc(
                    color = Color.Red,
                    startAngle = rotationAngle,
                    sweepAngle = 90f,
                    topLeft = topLeftOffset,
                    size = size,
                    useCenter = false,
                    style = stroke,
                )
                rotationAngle += 40
            }
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = when (currentState) {
                NONE -> "Test"
                STARTING -> "Starting..."
                MOTOR_TEST -> "testing motor..."
                TEC_TEST -> "testing Tec..."
                OTHER_TEST -> "testing other parts.."
                EVALUATING -> "evaluating..."
                COMPLETED -> "Completed"
            },
            color = textColor,
            fontSize = 14.sp,
            style = MaterialTheme.typography.overline
        )
    }
}