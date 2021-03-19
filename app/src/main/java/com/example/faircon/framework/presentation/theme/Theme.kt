package com.example.faircon.framework.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.faircon.SettingPreferences.Theme.*
import com.example.faircon.business.domain.state.StateMessage
import com.example.faircon.framework.presentation.components.MyCircularProgressIndicator
import com.example.faircon.framework.presentation.components.WiFiMonitor
import com.example.faircon.framework.presentation.components.snackbar.SnackbarController
import com.example.faircon.framework.presentation.components.stateMessageHandler.HandleMessageUiType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main

private val LightColorPalette = lightColors(
    primary = blue500,
    primaryVariant = blue800,
    secondary = blue500,
    secondaryVariant = blue800,
    background = Color.White,
    surface = Color.White,
    error = red500,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

private val DarkColorPalette = darkColors(
    primary = blue300,
    primaryVariant = blue800,
    secondary = blue300,
    background = darkBackground,
    surface = darkSurface,
    error = red300,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

val snackbarController = SnackbarController(CoroutineScope(Main))

@Composable
fun FairconTheme(
    theme: Int,
    isWiFiAvailable: Boolean = true,
    displayProgressBar: Boolean = false,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    stateMessage: StateMessage?,
    removeStateMessage: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val colour = when (theme) {
        DEFAULT_VALUE -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        DARK_VALUE -> DarkColorPalette
        LIGHT_VALUE -> LightColorPalette
        else -> DarkColorPalette
    }

    MaterialTheme(
        colors = colour,
        typography = typography,
        shapes = shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column {
                WiFiMonitor(isWiFiAvailable = isWiFiAvailable)
                content()
            }

            MyCircularProgressIndicator(
                isDisplayed = displayProgressBar,
                modifier = Modifier.align(Alignment.Center)
            )

            HandleMessageUiType(
                stateMessage = stateMessage,
                scaffoldState = scaffoldState,
                removeStateMessage = removeStateMessage
            )
        }
    }
}
