package com.example.faircon.framework.presentation.navigation

sealed class Navigation(
    val route: String,
){
    object Connect: Navigation("Connect")

    object Mode: Navigation("Mode")

    object Cooling: Navigation("Cooling")
    object CoolingDetail: Navigation("CoolingDetail")

    object Heating: Navigation("Heating")
    object HeatingDetail: Navigation("HeatingDetail")

    object Fan: Navigation("Fan")
    object FanDetail: Navigation("FanDetail")

    object Settings: Navigation("Settings")

    object Diagnostics: Navigation("Diagnostics")
    object Health: Navigation("Health")
    object Performance: Navigation("Performance")
}