package com.example.faircon.framework.presentation.navigation

sealed class Navigation(
    val route: String,
){
    object Connect: Navigation("Connect")

    object Mode: Navigation("Mode")

    object Cooling: Navigation("Cooling")

    object Heating: Navigation("Heating")

    object Fan: Navigation("Fan")

    object Settings: Navigation("Settings")
}