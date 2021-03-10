package com.example.faircon.framework.presentation.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faircon.framework.presentation.navigation.AccountScreen.*
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            val accountViewModel: AccountViewModel = viewModel()

            FairconTheme(
                isDark = isDark,
                scaffoldState = scaffoldState,
                stateMessage = accountViewModel.stateMessage.value,
                removeStateMessage = { accountViewModel.removeStateMessage() }
            ) {

                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { scaffoldState.snackbarHostState }
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = HomeAccount.route
                    ) {

                        composable(route = HomeAccount.route) {
                            AccountScreen(
                                navController = navController,
                                viewModel = accountViewModel
                            )
                        }

                        composable(route = UpdateAccount.route) {
                            UpdateAccountScreen(viewModel = accountViewModel)
                        }

                        composable(route = ResetPassword.route) {
                            ChangePasswordScreen(
                                navController = navController,
                                viewModel = accountViewModel
                            )
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