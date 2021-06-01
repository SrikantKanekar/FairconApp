package com.example.faircon.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.faircon.presentation.components.FairconConnectionState.*
import com.example.faircon.presentation.theme.connected
import com.example.faircon.presentation.theme.red500
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun FairconConnection(
    fairconConnection: Boolean,
) {
    var fairconConnectionState by remember { mutableStateOf(NONE) }

    LaunchedEffect(fairconConnection) {
        when (fairconConnection) {
            true -> {
                if (fairconConnectionState == DISCONNECTED) {
                    fairconConnectionState = CONNECTING
                    delay(2000)
                    fairconConnectionState = NONE
                }
            }
            false -> fairconConnectionState = DISCONNECTED
        }
    }

    val transition = updateTransition(targetState = fairconConnectionState, label = null)

    val background by transition.animateColor(
        transitionSpec = { tween(1000) },
        label = "background"
    ) { state ->
        when (state) {
            NONE -> MaterialTheme.colors.background
            CONNECTING -> connected
            DISCONNECTED -> red500
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        visible = fairconConnectionState != NONE,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(1000)),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(1000))
    ) {
        Column(
            modifier = Modifier.background(background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = when (fairconConnectionState) {
                    NONE -> ""
                    CONNECTING -> "Connected"
                    DISCONNECTED -> "Not Connected to Faircon"
                },
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

enum class FairconConnectionState {
    NONE, CONNECTING, DISCONNECTED
}