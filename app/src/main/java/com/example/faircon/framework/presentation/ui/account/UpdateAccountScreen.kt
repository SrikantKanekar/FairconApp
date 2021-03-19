package com.example.faircon.framework.presentation.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.faircon.framework.presentation.components.textField.MyUsernameTextField
import com.example.faircon.framework.presentation.components.textField.UsernameState

@Composable
fun UpdateAccountScreen(
    viewModel: AccountViewModel
) {
    val viewState = viewModel.viewState.collectAsState()
    val email = viewState.value.accountProperties?.email
    val username = viewState.value.accountProperties?.username

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(170.dp))

        if (email != null && username != null) {
            MyFormTitle(text = "Update")

            val focusRequester = remember { FocusRequester() }

            val emailState = remember { EmailState(email) }
            MyEmailTextField(
                emailState = emailState,
                onImeAction = { focusRequester.requestFocus() }
            )

            val usernameState =
                remember { UsernameState(username) }
            MyUsernameTextField(
                modifier = Modifier.padding(top = 10.dp)
                    .focusRequester(focusRequester),
                usernameState = usernameState,
                imeAction = ImeAction.Done,
                onImeAction = {
                    if (emailState.isValid && usernameState.isValid) {
                        viewModel.updateAccount(
                            emailState.text,
                            usernameState.text
                        )
                    }
                }
            )

            LoadingButton(
                modifier = Modifier.padding(25.dp),
                isLoading = viewModel.shouldDisplayProgressBar.value,
                text = "Update",
                enabled = emailState.isValid
                        && usernameState.isValid,
                onClick = {
                    viewModel.updateAccount(
                        emailState.text,
                        usernameState.text
                    )
                }
            )
        }
    }
}