package com.example.faircon.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.*
import com.example.faircon.framework.presentation.components.textField.*
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.auth.state.AuthStateEvent.RegisterAttemptEvent

class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                FairconTheme(
                    darkTheme = true,
                    displayProgressBar = false
                ) {

                    Scaffold {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(100.dp))

                            // LottieLoadingView(AmbientContext.current)

                            Text(
                                text = "Register",
                                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                            )

                            val usernameFocusRequester = remember { FocusRequester() }
                            val passwordFocusRequester = remember { FocusRequester() }
                            val confirmFocusRequester = remember { FocusRequester() }


                            val emailState = remember { EmailState() }
                            MyEmailTextField(
                                emailState = emailState,
                                onImeAction = { usernameFocusRequester.requestFocus() }
                            )

                            val usernameState = remember { UsernameState() }
                            MyUsernameTextField(
                                modifier = Modifier.focusRequester(usernameFocusRequester),
                                usernameState = usernameState,
                                onImeAction = { passwordFocusRequester.requestFocus() }
                            )

                            val passwordState = remember { PasswordState() }
                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(passwordFocusRequester),
                                passwordState = passwordState,
                                onImeAction = { confirmFocusRequester.requestFocus() }
                            )

                            val confirmPasswordState =
                                remember { ConfirmPasswordState(passwordState) }
                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(confirmFocusRequester),
                                passwordState = confirmPasswordState,
                                label = "Confirm Password",
                                imeAction = ImeAction.Done,
                                onImeAction = {

                                    if (emailState.isValid && usernameState.isValid
                                        && passwordState.isValid && confirmPasswordState.isValid){
                                        register(
                                            emailState.text,
                                            usernameState.text,
                                            passwordState.text,
                                            confirmPasswordState.text
                                        )
                                    }
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "Register",
                                enabled = emailState.isValid
                                        && usernameState.isValid
                                        && passwordState.isValid
                                        && confirmPasswordState.isValid,
                                onClick = {
                                    register(
                                        emailState.text,
                                        usernameState.text,
                                        passwordState.text,
                                        confirmPasswordState.text
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
    ) {
        viewModel.setStateEvent(
            RegisterAttemptEvent(
                email,
                username,
                password,
                confirmPassword
            )
        )
    }
}