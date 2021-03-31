package com.example.faircon.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.ui.controller.ControllerScreen
import com.example.faircon.framework.presentation.ui.home.HomeScreen
import com.example.faircon.framework.presentation.ui.home.HomeViewModel
import com.example.faircon.framework.presentation.ui.settings.SettingsScreen
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

                composable(route = Home.route) { backStackEntry ->
                    val homeViewModel = hiltNavGraphViewModel<HomeViewModel>(backStackEntry)
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                        navController = navController
                    )
                }

                composable(route = Controller.route) {
                    ControllerScreen(
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                    )
                }

                composable(route = Settings.route) {
                    SettingsScreen(
                        theme = appTheme.value,
                        isWiFiAvailable = wiFiConnectivityManager.isWiFiAvailable,
                        scaffoldState = scaffoldState,
                    )
                }
            }
        }
    }
}