package com.example.faircon.framework.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.MyFormTitle
import com.example.faircon.framework.presentation.components.textField.*

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        // LottieLoadingView(AmbientContext.current)

        MyFormTitle(text = "Register")

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
                    viewModel.register(
                        emailState.text,
                        usernameState.text,
                        passwordState.text,
                        confirmPasswordState.text
                    )
                }
            }
        )

        LoadingButton(
            modifier = Modifier.padding(25.dp),
            isLoading = viewModel.shouldDisplayProgressBar.value,
            text = "Register",
            enabled = emailState.isValid
                    && usernameState.isValid
                    && passwordState.isValid
                    && confirmPasswordState.isValid,
            onClick = {
                viewModel.register(
                    emailState.text,
                    usernameState.text,
                    passwordState.text,
                    confirmPasswordState.text
                )
            }
        )
    }
}