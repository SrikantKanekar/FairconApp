package com.example.faircon.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.faircon.model.Mode
import com.example.faircon.presentation.theme.connect
import com.example.faircon.presentation.theme.darkSurface

@Composable
fun ModeButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    mode: Mode,
    imageVector: ImageVector,
    onClick: (Mode) -> Unit,
    navigate: () -> Unit
) {
    val transition = updateTransition(targetState = selected, label = null)

    // Navigate to the destination only after the animation completes
    if (selected && transition.currentState) navigate()

    val color by transition.animateColor(
        transitionSpec = { tween(1000) },
        label = "color"
    ) { state ->
        when (state) {
            true -> connect
            false -> darkSurface
        }
    }

    val tint by transition.animateColor(
        transitionSpec = { tween(1000) },
        label = "tint"
    ) { state ->
        when (state) {
            true -> Color.Black
            false -> Color.White
        }
    }

    Surface(
        modifier = modifier
            .size(100.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = { onClick(mode) }
            ),
        shape = CircleShape,
        color = color,
        elevation = 8.dp
    ) {
        Icon(
            modifier = Modifier.padding(30.dp),
            imageVector = imageVector,
            contentDescription = mode.name,
            tint = tint
        )
    }
}