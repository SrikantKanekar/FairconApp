package com.example.faircon.framework.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.faircon.framework.presentation.components.MyIcon
import com.example.faircon.framework.presentation.navigation.AccountScreen
import com.example.faircon.framework.presentation.navigation.MainScreen
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.ui.auth.AuthActivity
import com.example.faircon.framework.presentation.ui.main.account.AccountScreen
import com.example.faircon.framework.presentation.ui.main.account.AccountViewModel
import com.example.faircon.framework.presentation.ui.main.account.ChangePasswordScreen
import com.example.faircon.framework.presentation.ui.main.account.UpdateAccountScreen
import com.example.faircon.framework.presentation.ui.main.controller.ControllerScreen
import com.example.faircon.framework.presentation.ui.main.controller.ControllerViewModel
import com.example.faircon.framework.presentation.ui.main.home.HomeScreen
import com.example.faircon.framework.presentation.ui.main.home.HomeViewModel
import com.example.faircon.framework.presentation.ui.main.settings.SettingsScreen
import com.example.faircon.framework.presentation.ui.main.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val items = listOf(
        MainScreen.Account,
        MainScreen.Setting
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

            val scaffoldState = rememberScaffoldState()

            val authViewModel: AccountViewModel = viewModel()
            val settingViewModel: SettingsViewModel = viewModel()

            val stateMessage = authViewModel.stateMessage.value

            FairconTheme(
                darkTheme = isDark.value,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                scaffoldState = scaffoldState,
                stateMessage = stateMessage,
                removeStateMessage = { authViewModel.removeStateMessage() }
            ) {

                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { scaffoldState.snackbarHostState },
                    bottomBar = {
                        if (currentRoute == MainScreen.Home.route){

                            BottomAppBar(
                                cutoutShape = CircleShape
                            ) {

                                items.forEach { screen ->
                                    IconButton(
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo = navController.graph.startDestination
                                                launchSingleTop = true
                                            }
                                        }
                                    ) {
                                        when (screen) {
                                            MainScreen.Account -> MyIcon(imageVector = Icons.Default.Person)
                                            MainScreen.Setting -> MyIcon(imageVector = Icons.Default.Settings)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        if (currentRoute == MainScreen.Home.route)
                        FloatingActionButton(
                            onClick = { navController.navigate(MainScreen.Controller.route) }
                        ) {
                            MyIcon(imageVector = Icons.Default.SettingsRemote)
                        }
                    },
                    isFloatingActionButtonDocked = true
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = MainScreen.Home.route
                    ) {

                        composable(route = MainScreen.Home.route) { navBackStackEntry ->
                            val factory =
                                HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                            val viewModel: HomeViewModel = viewModel("HomeViewModel", factory)
                            HomeScreen(viewModel = viewModel)
                        }

                        composable(route = MainScreen.Controller.route) { navBackStackEntry ->
                            val factory =
                                HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                            val viewModel: ControllerViewModel =
                                viewModel("ControllerViewModel", factory)
                            ControllerScreen(viewModel = viewModel)
                        }

                        navigation(
                            route = MainScreen.Account.route,
                            startDestination = AccountScreen.HomeAccount.route
                        ) {


                            composable(route = AccountScreen.HomeAccount.route) {
                                AccountScreen(
                                    navController = navController,
                                    viewModel = authViewModel
                                )
                            }


                            composable(route = AccountScreen.UpdateAccount.route) {
                                UpdateAccountScreen(viewModel = authViewModel)
                            }


                            composable(route = AccountScreen.ResetPassword.route) {
                                ChangePasswordScreen(
                                    navController = navController,
                                    viewModel = authViewModel
                                )
                            }
                        }

                        composable(route = MainScreen.Setting.route) {
                            SettingsScreen(settingViewModel)
                        }
                    }
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

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}