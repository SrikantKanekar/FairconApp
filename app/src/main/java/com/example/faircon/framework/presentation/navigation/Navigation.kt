package com.example.faircon.framework.presentation.navigation

sealed class Navigation(
    val route: String,
){
    object Home: Navigation("Home")

    object Controller: Navigation("Controller")

    object Settings: Navigation("Setting")
}