package com.example.faircon.framework.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.faircon.R

@Composable
fun LauncherScreenLogo() {
    AppLogo(isDisplayed = true)
}

@Composable
fun AppLogo(
    isDisplayed: Boolean,
    size: Dp = 110.dp
) {
    if (isDisplayed) {
        val painterResource = painterResource(R.mipmap.ic_launcher_round)

        Image(
            modifier = Modifier.preferredSize(size),
            painter = painterResource,
            contentDescription = "Logo"
        )
    }
}