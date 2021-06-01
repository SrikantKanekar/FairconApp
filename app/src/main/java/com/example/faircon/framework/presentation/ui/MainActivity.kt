package com.example.faircon.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.connect.ConnectScreen
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel
import com.example.faircon.framework.presentation.ui.cooling.CoolingDetailScreen
import com.example.faircon.framework.presentation.ui.cooling.CoolingScreen
import com.example.faircon.framework.presentation.ui.diagnostics.DiagnosticScreen
import com.example.faircon.framework.presentation.ui.diagnostics.health.HealthScreen
import com.example.faircon.framework.presentation.ui.diagnostics.performance.PerformanceScreen
import com.example.faircon.framework.presentation.ui.fan.FanDetailScreen
import com.example.faircon.framework.presentation.ui.fan.FanScreen
import com.example.faircon.framework.presentation.ui.heating.HeatingDetailScreen
import com.example.faircon.framework.presentation.ui.heating.HeatingScreen
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

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            FairconTheme(
                theme = appTheme,
                fairconConnection = if (currentRoute == Connect.route || currentRoute == Settings.route) true else webSocket.isOpen.value
            ) {
                Scaffold {

                    NavHost(
                        navController = navController,
                        startDestination = Connect.route
                    ) {

                        composable(route = Connect.route) {
                            val connectViewModel = hiltViewModel<ConnectViewModel>()
                            ConnectScreen(
                                viewModel = connectViewModel,
                                navigateToMode = {
                                    navController.navigate(Mode.route) {
                                        popUpTo(Connect.route) { inclusive = true }
                                    }
                                },
                                navigateToSettings = { navController.navigate(Settings.route) }
                            )
                        }

                        composable(route = Mode.route) {
                            val modeViewModel = hiltViewModel<ModeViewModel>()
                            ModeScreen(
                                viewModel = modeViewModel,
                                navigateToDiagnostics = { navController.navigate(Diagnostics.route) },
                                navigateToCooling = { navController.navigate(Cooling.route) },
                                navigateToHeating = { navController.navigate(Heating.route) },
                                navigateToFan = { navController.navigate(Fan.route) }
                            )
                        }

                        navigation(
                            startDestination = Cooling.route,
                            route = "CoolingRoute"
                        ) {
                            composable(route = Cooling.route) {
                                CoolingScreen(
                                    webSocket = webSocket,
                                    navigateToDetails = { navController.navigate(CoolingDetail.route) }
                                )
                            }

                            composable(route = CoolingDetail.route) {
                                CoolingDetailScreen(webSocket = webSocket)
                            }
                        }

                        navigation(
                            startDestination = Heating.route,
                            route = "HeatingRoute"
                        ) {
                            composable(route = Heating.route) {
                                HeatingScreen(
                                    webSocket = webSocket,
                                    navigateToDetails = { navController.navigate(HeatingDetail.route) }
                                )
                            }
                            composable(route = HeatingDetail.route) {
                                HeatingDetailScreen(webSocket = webSocket)
                            }
                        }

                        navigation(
                            startDestination = Fan.route,
                            route = "FanRoute"
                        ) {
                            composable(route = Fan.route) {
                                FanScreen(
                                    webSocket = webSocket,
                                    navigateToDetails = { navController.navigate(FanDetail.route) }
                                )
                            }

                            composable(route = FanDetail.route) {
                                FanDetailScreen(webSocket = webSocket)
                            }
                        }

                        composable(route = Settings.route) {
                            SettingsScreen()
                        }

                        composable(route = Diagnostics.route) {
                            DiagnosticScreen(
                                navigateToHealth = { navController.navigate(Health.route) },
                                navigateToPerformance = { navController.navigate(Performance.route) }
                            )
                        }

                        composable(route = Health.route) {
                            HealthScreen()
                        }

                        composable(route = Performance.route) {
                            PerformanceScreen()
                        }
                    }
                }
            }
        }
    }
}