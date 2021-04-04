package com.example.faircon.framework.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.faircon.framework.presentation.navigation.Navigation.*
import com.example.faircon.framework.presentation.ui.connect.ConnectScreen
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel
import com.example.faircon.framework.presentation.ui.cooling.CoolingDetailScreen
import com.example.faircon.framework.presentation.ui.cooling.CoolingScreen
import com.example.faircon.framework.presentation.ui.cooling.CoolingViewModel
import com.example.faircon.framework.presentation.ui.fan.FanScreen
import com.example.faircon.framework.presentation.ui.fan.FanViewModel
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

                composable(route = Connect.route) { backStackEntry ->
                    val connectViewModel = hiltNavGraphViewModel<ConnectViewModel>(backStackEntry)
                    ConnectScreen(
                        theme = appTheme.value,
                        scaffoldState = scaffoldState,
                        viewModel = connectViewModel,
                        navController = navController
                    )
                }

                composable(route = Mode.route) { backStackEntry ->
                    val modeViewModel = hiltNavGraphViewModel<ModeViewModel>(backStackEntry)
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
                        val coolingViewModel =
                            navController.hiltNavGraphViewModel<CoolingViewModel>("CoolingRoute")
                        CoolingScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = coolingViewModel,
                            navController = navController
                        )
                    }

                    composable(route = CoolingDetail.route) {
                        val coolingViewModel =
                            navController.hiltNavGraphViewModel<CoolingViewModel>("CoolingRoute")
                        CoolingDetailScreen(
                            theme = appTheme.value,
                            isWifiAvailable = faircon.isAvailable.value == true,
                            scaffoldState = scaffoldState,
                            viewModel = coolingViewModel
                        )
                    }
                }

                composable(route = Heating.route) { backStackEntry ->
                    val heatingViewModel = hiltNavGraphViewModel<HeatingViewModel>(backStackEntry)
                    HeatingScreen(
                        theme = appTheme.value,
                        isWifiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState,
                        viewModel = heatingViewModel
                    )
                }

                composable(route = Fan.route) { backStackEntry ->
                    val fanViewModel = hiltNavGraphViewModel<FanViewModel>(backStackEntry)
                    FanScreen(
                        theme = appTheme.value,
                        isWifiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState,
                        viewModel = fanViewModel
                    )
                }

                composable(route = Settings.route) {
                    SettingsScreen(
                        theme = appTheme.value,
                        isWiFiAvailable = faircon.isAvailable.value == true,
                        scaffoldState = scaffoldState,
                    )
                }
            }
        }
    }
}