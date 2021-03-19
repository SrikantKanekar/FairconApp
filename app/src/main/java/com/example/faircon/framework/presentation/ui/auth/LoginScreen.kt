package com.example.faircon.framework.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.MyFormTitle
import com.example.faircon.framework.presentation.components.textField.EmailState
import com.example.faircon.framework.presentation.components.textField.MyEmailTextField
import com.example.faircon.framework.presentation.components.textField.MyPasswordTextField
import com.example.faircon.framework.presentation.components.textField.PasswordState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(170.dp))

        MyFormTitle(text = "Login")

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
                if (emailState.isValid && passwordState.isValid) {
                    viewModel.login(
                        emailState.text,
                        passwordState.text,
                    )
                }
            }
        )

        LoadingButton(
            modifier = Modifier.padding(25.dp),
            isLoading = viewModel.shouldDisplayProgressBar.value,
            text = "LOGIN",
            enabled = emailState.isValid
                    && passwordState.isValid,
            onClick = {
                viewModel.login(
                    emailState.text,
                    passwordState.text,
                )
            }
        )
    }
}