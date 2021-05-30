package com.example.faircon.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.ui.connect.ConnectScreen
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel
import com.example.faircon.framework.presentation.ui.cooling.CoolingDetailScreen
import com.example.faircon.framework.presentation.ui.cooling.CoolingScreen
import com.example.faircon.framework.presentation.ui.cooling.CoolingViewModel
import com.example.faircon.framework.presentation.ui.diagnostics.DiagnosticScreen
import com.example.faircon.framework.presentation.ui.diagnostics.health.HealthScreen
import com.example.faircon.framework.presentation.ui.diagnostics.performance.PerformanceScreen
import com.example.faircon.framework.presentation.ui.fan.FanDetailScreen
import com.example.faircon.framework.presentation.ui.fan.FanScreen
import com.example.faircon.framework.presentation.ui.fan.FanViewModel
import com.example.faircon.framework.presentation.ui.heating.HeatingDetailScreen
import com.example.faircon.framework.presentation.ui.heating.HeatingScreen
import com.example.faircon.framework.presentation.ui.heating.HeatingViewModel
import com.example.faircon.framework.presentation.ui.mode.ModeScreen
import com.example.faircon.framework.presentation.ui.mode.ModeViewModel
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
                startDestination = Connect.route
            ) {

                composable(route = Connect.route) {
                    val connectViewModel = hiltViewModel<ConnectViewModel>()
                    ConnectScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState,
                        viewModel = connectViewModel,
                        navController = navController
                    )
                }

                composable(route = Mode.route) {
                    val modeViewModel = hiltViewModel<ModeViewModel>()
                    ModeScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState,
                        viewModel = modeViewModel,
                        navController = navController
                    )
                }

                navigation(
                    startDestination = Cooling.route,
                    route = "CoolingRoute"
                ) {
                    composable(route = Cooling.route) {
                        val coolingViewModel = hiltViewModel<CoolingViewModel>(
                            navController.getBackStackEntry("CoolingRoute")
                        )
                        CoolingScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = coolingViewModel,
                            navController = navController
                        )
                    }

                    composable(route = CoolingDetail.route) {
                        val coolingViewModel = hiltViewModel<CoolingViewModel>(
                            navController.getBackStackEntry("CoolingRoute")
                        )
                        CoolingDetailScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = coolingViewModel
                        )
                    }
                }

                navigation(
                    startDestination = Heating.route,
                    route = "HeatingRoute"
                ) {
                    composable(route = Heating.route) {
                        val heatingViewModel = hiltViewModel<HeatingViewModel>(
                            navController.getBackStackEntry("HeatingRoute")
                        )
                        HeatingScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = heatingViewModel,
                            navController = navController
                        )
                    }
                    composable(route = HeatingDetail.route) {
                        val heatingViewModel = hiltViewModel<HeatingViewModel>(
                            navController.getBackStackEntry("HeatingRoute")
                        )
                        HeatingDetailScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = heatingViewModel
                        )
                    }
                }

                navigation(
                    startDestination = Fan.route,
                    route = "FanRoute"
                ) {
                    composable(route = Fan.route) {
                        val fanViewModel = hiltViewModel<FanViewModel>(
                            navController.getBackStackEntry("FanRoute")
                        )
                        FanScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = fanViewModel,
                            navController = navController
                        )
                    }

                    composable(route = FanDetail.route) {
                        val fanViewModel = hiltViewModel<FanViewModel>(
                            navController.getBackStackEntry("FanRoute")
                        )
                        FanDetailScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = fanViewModel
                        )
                    }
                }

                composable(route = Settings.route) {
                    SettingsScreen(
                        theme = appTheme.value,
                        isWiFiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState,
                    )
                }

                composable(route = Diagnostics.route) {
                    DiagnosticScreen(
                        theme = appTheme.value,
                        isWifiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState,
                        navController = navController
                    )
                }

                composable(route = Health.route) {
                    HealthScreen(
                        theme = appTheme.value,
                        isWifiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState
                    )
                }

                composable(route = Performance.route) {
                    PerformanceScreen(
                        theme = appTheme.value,
                        isWifiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }
}