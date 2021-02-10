package com.example.faircon.framework.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SpotifyNotInstalledDialog(
    isDisplayed: Boolean,
    showDialog: (Boolean) -> Unit,
    downloadSpotify: () -> Unit,
    backToLauncher: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        text = "Please Download Spotify from playStore",
        confirmButton = {
            Button(
                onClick = {
                    showDialog(false)
                    downloadSpotify()
                }
            ) {
                Text(text = "Get Spotify Free")
            }
        },
        dismissButton = { },
        onDismissRequest = {
            backToLauncher()
        }
    )
}

@Composable
fun FirebaseErrorDialog(
    isDisplayed: Boolean,
    text: String,
    showDialog: (Boolean) -> Unit,
    backToLauncher: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        text = text,
        confirmButton = {
            Button(
                onClick = {
                    showDialog(false)
                    backToLauncher()
                }
            ) {
                Text(text = "Retry")
            }
        },
        dismissButton = { },
        onDismissRequest = {
            backToLauncher()
        }
    )
}

@Composable
fun SpotifyTokenErrorDialog(
    isDisplayed: Boolean,
    text: String,
    showDialog: (Boolean) -> Unit,
    backToLauncher: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        text = text,
        confirmButton = {
            Button(
                onClick = {
                    showDialog(false)
                    backToLauncher()
                }
            ) {
                Text(text = "Retry")
            }
        },
        dismissButton = { },
        onDismissRequest = {
            backToLauncher()
        }
    )
}

@Composable
fun MyDialog(
    isDisplayed: Boolean,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (isDisplayed) {
        AlertDialog(
            text = { Text(text = text) },
            confirmButton = { confirmButton() },
            dismissButton = { dismissButton() },
            onDismissRequest = { onDismissRequest() }
        )
    }
}