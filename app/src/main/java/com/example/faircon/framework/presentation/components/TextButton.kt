package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun MyLinkTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
        MyLinkText(
            modifier = modifier.clickable { onClick() },
            text = text
        )
}

@Composable
fun MyTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier.clickable { onClick() },
        text = text,
        fontSize = 25.sp,
        style = MaterialTheme.typography.body1
    )
}