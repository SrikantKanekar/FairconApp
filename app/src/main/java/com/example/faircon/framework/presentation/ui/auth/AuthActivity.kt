package com.example.faircon.framework.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faircon.business.interactors.auth.CheckPreviousUser.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.example.faircon.framework.presentation.components.image.AppLogo
import com.example.faircon.framework.presentation.navigation.AuthScreen
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.ui.auth.passwordReset.PasswordResetScreen
import com.example.faircon.framework.presentation.ui.auth.passwordReset.PasswordResetSuccessScreen
import com.example.faircon.framework.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            val authViewModel: AuthViewModel = viewModel()

            if (authViewModel.stateMessage.value?.response?.message.equals(
                    RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
                )
            ) {
                onFinishCheckPreviousUser()
            }

            FairconTheme(
                isDark = isDark,
                scaffoldState = scaffoldState,
                stateMessage = authViewModel.stateMessage.value,
                removeStateMessage = { authViewModel.removeStateMessage() }
            ) {

                if (authViewModel.checkPreviousUser.value) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState }
                    ) {

                        NavHost(
                            navController = navController,
                            startDestination = AuthScreen.LauncherScreen.route
                        ) {

                            composable(route = AuthScreen.LauncherScreen.route) {
                                LauncherScreen(navController = navController)
                            }

                            composable(route = AuthScreen.LoginScreen.route) {
                                LoginScreen(viewModel = authViewModel)
                            }

                            composable(route = AuthScreen.RegisterScreen.route) {
                                RegisterScreen(viewModel = authViewModel)
                            }

                            composable(route = AuthScreen.PasswordResetScreen.route) {
                                PasswordResetScreen(
                                    navController = navController,
                                    viewModel = authViewModel
                                )
                            }

                            composable(route = AuthScreen.PasswordResetSuccessScreen.route) {
                                PasswordResetSuccessScreen(navController = navController)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppLogo(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, { viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, { token ->
            token.let { authToken ->
                if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousUser() {
        viewModel.checkPreviousUser.value = true
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}