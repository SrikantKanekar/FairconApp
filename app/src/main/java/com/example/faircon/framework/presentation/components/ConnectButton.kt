package com.example.faircon.framework.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faircon.framework.presentation.theme.connect
import com.example.faircon.framework.presentation.theme.connected
import com.example.faircon.framework.presentation.theme.connecting
import com.example.faircon.framework.presentation.theme.retry
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel.ConnectionState.*

@Composable
fun ConnectButton(
    modifier: Modifier = Modifier,
    connectionState: ConnectViewModel.ConnectionState,
    onClick: () -> Unit,
    navigate: () -> Unit
) {
    val transition = updateTransition(targetState = connectionState, label = null)

    if (transition.currentState == CONNECTED) navigate()

    val color by transition.animateColor(
        transitionSpec = { tween(1500) },
        label = "ColorAnimation"
    ) { state ->
        when (state) {
            CONNECT -> connect
            CONNECTING -> connecting
            CONNECTED -> connected
            RETRY -> retry
        }
    }

    val textColor by transition.animateColor(
        transitionSpec = { tween(1500) },
        label = "TextColorAnimation"
    ) { state ->
        when (state) {
            CONNECT -> Color.Black
            CONNECTING -> Color.Black
            CONNECTED -> Color.Black
            RETRY -> Color.White
        }
    }

    val sweepAngle by transition.animateFloat(
        transitionSpec = {
            when {
                CONNECTING isTransitioningTo CONNECTED -> tween(0)
                CONNECTING isTransitioningTo RETRY -> tween(0)
                else -> tween(5000)
            }
        },
        label = "angle"
    ) { state ->
        when (state) {
            CONNECT -> 0f
            CONNECTING -> 360f
            CONNECTED -> 0f
            RETRY -> 0f
        }
    }

    val radius = remember { Animatable(0F) }
    LaunchedEffect(Unit) {
        radius.animateTo(
            targetValue = 260F,
            animationSpec = tween(durationMillis = 800, delayMillis = 250)
        )
    }

    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onClick
                )
        ) {
            drawCircle(
                color = color.copy(alpha = 0.8F),
                radius = radius.value
            )
            drawCircle(
                color = color.copy(alpha = 0.4F),
                radius = radius.value * 0.8F
            )

            if (connectionState == CONNECTING) {
                val stroke = Stroke(30f)
                drawArc(
                    color = Color.Red,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = stroke
                )
            }
        }

        if (!radius.isRunning) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = connectionState.name,
                fontSize = 17.sp,
                color = textColor
            )
        }
    }

}