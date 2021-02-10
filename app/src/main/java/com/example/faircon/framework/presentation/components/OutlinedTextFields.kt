package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MyUsernameTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {

    MyOutlinedTextField(
        modifier = modifier,
        initialValue = initialValue,
        onValueChange = { onValueChange(it) },
        label = "Username",
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = imeAction),
        onImeAction = onImeAction,
    )
}

@Composable
fun MyEmailTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    MyOutlinedTextField(
        modifier = modifier,
        initialValue = initialValue,
        onValueChange = { onValueChange(it) },
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
    initialValue: String = "",
    onValueChange: (String) -> Unit,
    label: String = "Password",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {

    val passwordVisibility = remember { mutableStateOf(false) }

    MyOutlinedTextField(
        modifier = modifier,
        initialValue = initialValue,
        onValueChange = { onValueChange(it) },
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
    initialValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: () -> Unit = {}
) {

    val value = remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value.value,
        onValueChange = {
            onValueChange(it)
            value.value = it
        },
        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onImeActionPerformed = { action, softKeyboardController ->
            if (action == ImeAction.Done){
                softKeyboardController?.hideSoftwareKeyboard()
            }
            onImeAction()
        },
        maxLines = 1
    )
}
