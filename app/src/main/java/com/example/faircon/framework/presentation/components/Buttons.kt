package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.theme.typography
import java.util.*

@Composable
fun LoadingButton(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    onClick: () -> Unit
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = modifier
        )
    } else {
        MyButton(
            modifier = modifier,
            text = text,
            enabled = enabled,
            onClick = onClick
        )
    }
}

@Composable
fun SelectableButton(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected){
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.primary
            }
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .height(45.dp)
            .clip(CircleShape),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = text.toUpperCase(Locale.getDefault()),
            style = typography.button,
            color = MaterialTheme.colors.onPrimary
        )
    }
}