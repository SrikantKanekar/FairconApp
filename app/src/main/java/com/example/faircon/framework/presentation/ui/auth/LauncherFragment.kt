package com.example.faircon.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.framework.presentation.components.LauncherScreenLogo
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.components.MyTextButton
import com.example.faircon.framework.presentation.theme.FairconTheme

class LauncherFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FairconTheme(
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(100.dp))

                        LauncherScreenLogo()

                        MyTextButton(
                            text = "Login",
                            onClick = { navLogin() }
                        )

                        MyTextButton(
                            text = "Register",
                            onClick = { navRegistration() }
                        )

                        MyLinkTextButton(
                            text = "Forgot Password",
                            onClick = { navForgotPassword() }
                        )
                    }
                }
            }
        }
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

    private fun navForgotPassword() {
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }
}