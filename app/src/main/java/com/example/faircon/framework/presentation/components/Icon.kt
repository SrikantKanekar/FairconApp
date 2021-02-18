package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MyIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    size: Dp = 20.dp
) {
    Icon(
        modifier = modifier.preferredSize(size),
        imageVector = imageVector,
        contentDescription = ""
    )
}