package com.example.faircon.framework.presentation.ui.account

import androidx.compose.foundation.layout.*
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
import com.example.faircon.framework.presentation.components.textField.ConfirmPasswordState
import com.example.faircon.framework.presentation.components.textField.MyPasswordTextField
import com.example.faircon.framework.presentation.components.textField.PasswordState

@Composable
fun ChangePasswordScreen(
    viewModel: AccountViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(140.dp))

        MyFormTitle(text = "Change Password")

        val newPasswordFocusRequester = remember { FocusRequester() }
        val confirmPasswordFocusRequester = remember { FocusRequester() }

        val currentPasswordState = remember { PasswordState() }
        MyPasswordTextField(
            passwordState = currentPasswordState,
            label = "Current password",
            onImeAction = { newPasswordFocusRequester.requestFocus() }
        )

        val newPasswordState = remember { PasswordState() }
        MyPasswordTextField(
            modifier = Modifier.focusRequester(newPasswordFocusRequester),
            passwordState = newPasswordState,
            label = "New password",
            onImeAction = { confirmPasswordFocusRequester.requestFocus() }
        )

        val confirmPasswordState =
            remember { ConfirmPasswordState(newPasswordState) }
        MyPasswordTextField(
            modifier = Modifier.focusRequester(confirmPasswordFocusRequester),
            passwordState = confirmPasswordState,
            label = "Confirm new password",
            imeAction = ImeAction.Done,
            onImeAction = {
                if (currentPasswordState.isValid && newPasswordState.isValid
                    && confirmPasswordState.isValid
                ) {
                    viewModel.changePassword(
                        currentPasswordState.text,
                        newPasswordState.text,
                        confirmPasswordState.text
                    )
                }
            }
        )

        LoadingButton(
            modifier = Modifier.padding(25.dp),
            isLoading = viewModel.shouldDisplayProgressBar.value,
            text = "Update password",
            enabled = currentPasswordState.isValid
                    && newPasswordState.isValid
                    && confirmPasswordState.isValid,
            onClick = {
                viewModel.changePassword(
                    currentPasswordState.text,
                    newPasswordState.text,
                    confirmPasswordState.text
                )
            }
        )
    }
}