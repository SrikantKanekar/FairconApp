package com.example.faircon.framework.presentation.navigation

sealed class AuthScreen(
    val route: String,
){
    object LauncherScreen: AuthScreen("LauncherScreen")

    object LoginScreen: AuthScreen("LoginScreen")

    object RegisterScreen: AuthScreen("RegisterScreen")

    object PasswordResetScreen: AuthScreen("PasswordResetScreen")

    object PasswordResetSuccessScreen: AuthScreen("PasswordResetSuccessScreen")
}