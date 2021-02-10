package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyLinkTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier.padding(5.dp),
        onClick = onClick
    ) {
        MyLinkText(text = text)
    }
}


@Composable
fun MyTextButton(
    modifier: Modifier = Modifier,
    text:String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier.padding(5.dp),
        onClick = onClick
    ) {
        MyText(
            text = text,
            fontSize = 25.sp,
            style = MaterialTheme.typography.body1
        )
    }
}