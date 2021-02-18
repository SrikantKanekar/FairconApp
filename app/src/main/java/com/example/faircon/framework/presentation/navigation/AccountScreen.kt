package com.example.faircon.framework.presentation.navigation

sealed class AccountScreen(
    val route: String,
){
    object HomeAccount: AccountScreen("HomeAccount")

    object UpdateAccount: AccountScreen("UpdateAccount")

    object ResetPassword: AccountScreen("ResetPassword")
}