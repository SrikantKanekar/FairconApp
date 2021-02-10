package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MyFab(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: ()-> Unit

) {
    FloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = onClick
    ) {
        Icon(imageVector = imageVector, contentDescription = "")
    }
}
