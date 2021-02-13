package com.example.faircon.framework.presentation.ui.auth

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.textField.EmailState
import com.example.faircon.framework.presentation.components.textField.MyEmailTextField
import com.example.faircon.framework.presentation.components.textField.MyPasswordTextField
import com.example.faircon.framework.presentation.components.textField.PasswordState
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.auth.state.AuthStateEvent.LoginAttemptEvent

class LoginFragment : BaseAuthFragment() {

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

                            Spacer(modifier = Modifier.height(170.dp))

                            // LottieLoadingView(AmbientContext.current)

                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                            )

                            val focusRequester = remember { FocusRequester() }


                            val emailState = remember { EmailState() }
                            MyEmailTextField(
                                emailState = emailState,
                                onImeAction = { focusRequester.requestFocus() }
                            )

                            val passwordState = remember { PasswordState() }
                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(focusRequester),
                                passwordState = passwordState,
                                imeAction = ImeAction.Done,
                                onImeAction = {
                                    if (emailState.isValid && passwordState.isValid){
                                        login(
                                            emailState.text,
                                            passwordState.text,
                                        )
                                    }
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "LOGIN",
                                enabled = emailState.isValid
                                        && passwordState.isValid,
                                onClick = {
                                    login(
                                        emailState.text,
                                        passwordState.text,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModel.setStateEvent(
            LoginAttemptEvent(email, password)
        )
    }
}

@Composable
fun LottieLoadingView(context: Context) {
    val lottieView = remember {
        LottieAnimationView(context).apply {
            setAnimation("working.json")
            repeatCount = ValueAnimator.INFINITE
        }
    }
    AndroidView({ lottieView }, modifier = Modifier.height(150.dp)){
        it.playAnimation()
    }
}