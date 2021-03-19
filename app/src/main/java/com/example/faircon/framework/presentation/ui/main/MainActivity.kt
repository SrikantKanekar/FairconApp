package com.example.faircon.framework.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faircon.framework.presentation.navigation.MainScreen.*
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.ui.account.AccountActivity
import com.example.faircon.framework.presentation.ui.auth.AuthActivity
import com.example.faircon.framework.presentation.ui.main.controller.ControllerScreen
import com.example.faircon.framework.presentation.ui.main.controller.ControllerViewModel
import com.example.faircon.framework.presentation.ui.main.home.HomeScreen
import com.example.faircon.framework.presentation.ui.main.home.HomeViewModel
import com.example.faircon.framework.presentation.ui.main.settings.SettingsScreen
import com.example.faircon.framework.presentation.ui.main.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            NavHost(
                navController = navController,
                startDestination = Home.route
            ) {

                composable(route = Home.route) { navBackStackEntry ->
                    val homeViewModel: HomeViewModel =
                        hiltNavGraphViewModel(backStackEntry = navBackStackEntry)
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                        navController = navController,
                        navAccountActivity = { navAccountActivity() }
                    )
                }

                composable(route = Controller.route) { navBackStackEntry ->
                    val controllerViewModel: ControllerViewModel =
                        hiltNavGraphViewModel(backStackEntry = navBackStackEntry)
                    ControllerScreen(
                        controllerViewModel = controllerViewModel,
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                    )
                }

                composable(route = Settings.route) { navBackStackEntry ->
                    val settingsViewModel: SettingsViewModel =
                        hiltNavGraphViewModel(backStackEntry = navBackStackEntry)
                    SettingsScreen(
                        settingsViewModel = settingsViewModel,
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, { authToken ->
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}