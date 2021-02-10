package com.example.faircon.framework.presentation.ui.auth

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.MyEmailTextField
import com.example.faircon.framework.presentation.components.MyPasswordTextField
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.theme.typography
import com.example.faircon.framework.presentation.ui.auth.state.AuthStateEvent.LoginAttemptEvent
import java.util.*

class LoginFragment : BaseAuthFragment() {

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

                    val loginFields = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .loginFields

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

                            MyEmailTextField(
                                initialValue = loginFields.login_email,
                                onValueChange = { viewModel.setLoginEmail(it) },
                                onImeAction = {
                                    focusRequester.requestFocus()
                                }
                            )

                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(focusRequester),
                                initialValue = loginFields.login_password,
                                onValueChange = { viewModel.setLoginPassword(it) },
                                imeAction = ImeAction.Done,
                                onImeAction = {
                                    login(loginFields.login_email, loginFields.login_password)
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "LOGIN",
                                onClick = {
                                    login(
                                        loginFields.login_email,
                                        loginFields.login_password
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