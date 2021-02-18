package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileDetailText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.caption,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun MyValueText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun MyOverlineText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 18.sp,
        style = MaterialTheme.typography.overline
    )
}

@Composable
fun MyLinkText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colors.primary,
        fontSize = 12.sp,
        style = MaterialTheme.typography.overline
    )
}

@Composable
fun MyFormTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(top = 8.dp, bottom = 16.dp),
        text = text,
        fontSize = 25.sp,
        style = MaterialTheme.typography.body1
    )
}