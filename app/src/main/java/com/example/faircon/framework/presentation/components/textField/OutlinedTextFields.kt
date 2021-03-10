package com.example.faircon.framework.presentation.components.textField

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyUsernameTextField(
    modifier: Modifier = Modifier,
    usernameState: UsernameState,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {

    MyOutlinedTextField(
        modifier = modifier,
        textFieldState = usernameState,
        onValueChange = { usernameState.enableShowErrors() },
        label = "Username",
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = imeAction),
        onImeAction = onImeAction,
    )
}

@Composable
fun MyEmailTextField(
    modifier: Modifier = Modifier,
    emailState: EmailState,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    MyOutlinedTextField(
        modifier = modifier,
        textFieldState = emailState,
        label = "Email",
        placeholder = "abc@gmail.com",
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        onImeAction = onImeAction

    )
}

@Composable
fun MyPasswordTextField(
    modifier: Modifier = Modifier,
    passwordState: TextFieldState,
    label: String = "Password",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {

    val passwordVisibility = remember { mutableStateOf(false) }

    MyOutlinedTextField(
        modifier = modifier,
        textFieldState = passwordState,
        onValueChange = { passwordState.enableShowErrors() },
        label = label,
        placeholder = "password",
        leadingIcon = { Icon(imageVector = Icons.Default.VpnKey, contentDescription = "") },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    if (passwordVisibility.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = ""
                )
            }
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        onImeAction = onImeAction
    )
}

@Composable
fun MyOutlinedTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onValueChange: () -> Unit = {},
    label: String,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: () -> Unit = {}
) {

    val state = remember { textFieldState }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = state.text,
        onValueChange = { value ->
            state.text = value
            onValueChange()
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                val focused = focusState == FocusState.Active
                state.onFocusChange(focused)
                if (!focused) {
                    state.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        placeholder = { Text(text = placeholder) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = state.showErrors(),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() },
            onDone = {
                focusManager.clearFocus(true)
                onImeAction()
            }
        )
    )
    state.getError()?.let { error -> TextFieldError(textError = error) }
}


/**
 * To be removed when [TextField]s support error
 */
@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp)) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.error,
                fontSize = 12.sp
            )
        )
    }
}
