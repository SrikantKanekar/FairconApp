package com.example.faircon.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.*
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
                    displayProgressBar = false
                ) {

                    val registrationFields = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .registrationFields

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

                            MyEmailTextField(
                                initialValue = registrationFields.registration_email,
                                onValueChange = { viewModel.setRegistrationEmail(it) },
                                onImeAction = {
                                    usernameFocusRequester.requestFocus()
                                }
                            )

                            MyUsernameTextField(
                                modifier = Modifier.focusRequester(usernameFocusRequester),
                                initialValue = registrationFields.registration_username,
                                onValueChange = { viewModel.setRegistrationUsername(it) },
                                onImeAction = {
                                    passwordFocusRequester.requestFocus()
                                }
                            )

                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(passwordFocusRequester),
                                initialValue = registrationFields.registration_password,
                                onValueChange = { viewModel.setRegistrationPassword(it) },
                                onImeAction = {
                                    confirmFocusRequester.requestFocus()
                                }
                            )

                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(confirmFocusRequester),
                                initialValue = registrationFields.registration_confirm_password,
                                onValueChange = { viewModel.setRegistrationConfirmPassword(it) },
                                label = "Confirm Password",
                                imeAction = ImeAction.Done,
                                onImeAction = {
                                    register(
                                        registrationFields.registration_email,
                                        registrationFields.registration_username,
                                        registrationFields.registration_password,
                                        registrationFields.registration_confirm_password
                                    )
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "Register",
                                onClick = {
                                    register(
                                        registrationFields.registration_email,
                                        registrationFields.registration_username,
                                        registrationFields.registration_password,
                                        registrationFields.registration_confirm_password
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