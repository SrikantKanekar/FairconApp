package com.example.faircon.framework.presentation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.faircon.framework.presentation.components.*
import com.example.faircon.framework.presentation.components.snackbar.DefaultSnackbar
import java.util.*

private val LightColorPalette = lightColors(
    primary = blue300,
    primaryVariant = blue400,
    secondary = blue300,
    secondaryVariant = blue400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Red800
)

private val DarkColorPalette = darkColors(
    primary = blue300,
    primaryVariant = blue400,
    secondary = blue300,
    background = Color.Black,
    surface = graySurface,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Red300
)

@Composable
fun FairconTheme(
    darkTheme: Boolean,
    isNetworkAvailable: Boolean = true,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    messageQueue: Queue<GenericDialogInfo>? = null,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = typography,
        shapes = shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column{
                ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
                content()
            }

            MyCircularProgressIndicator(
                isDisplayed = displayProgressBar,
                modifier = Modifier.align(Alignment.Center)
            )

            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            ProcessMessageStack(
                messageQueue = messageQueue,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
fun ProcessMessageStack(
    messageQueue: Queue<GenericDialogInfo>?,
    onDismiss: () -> Unit,
) {
    messageQueue?.peek()?.let { dialogInfo ->
        GenericDialog(
            onDismiss = onDismiss,
            title = dialogInfo.title,
            description = dialogInfo.description,
            positiveAction = dialogInfo.positiveAction,
            negativeAction = dialogInfo.negativeAction
        )
    }
}