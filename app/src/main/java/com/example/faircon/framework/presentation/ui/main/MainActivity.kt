package com.example.faircon.framework.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    val factory =
                        HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val homeViewModel: HomeViewModel = viewModel("HomeViewModel", factory)
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        isDark = isDark,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                        navController = navController,
                        navAccountActivity = { navAccountActivity() }
                    )
                }

                composable(route = Controller.route) { navBackStackEntry ->
                    val factory =
                        HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val controllerViewModel: ControllerViewModel =
                        viewModel("ControllerViewModel", factory)
                    ControllerScreen(
                        controllerViewModel = controllerViewModel,
                        isDark = isDark,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                    )
                }

                composable(route = Setting.route) { navBackStackEntry ->
                    val factory =
                        HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val settingsViewModel: SettingsViewModel =
                        viewModel("SettingsViewModel", factory)
                    SettingsScreen(
                        settingsViewModel = settingsViewModel,
                        isDark = isDark,
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