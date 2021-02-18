package com.example.faircon.framework.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.components.MyTextButton
import com.example.faircon.framework.presentation.components.image.LauncherScreenLogo
import com.example.faircon.framework.presentation.navigation.AuthScreen

@Composable
fun LauncherScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(150.dp))

        LauncherScreenLogo()

        MyTextButton(
            modifier = Modifier.padding(top = 50.dp).padding(bottom = 10.dp),
            text = "Login",
            onClick = { navController.navigate(AuthScreen.LoginScreen.route) }
        )

        MyTextButton(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "Register",
            onClick = { navController.navigate(AuthScreen.RegisterScreen.route) }
        )

        MyLinkTextButton(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "Forgot Password",
            onClick = { navController.navigate(AuthScreen.PasswordResetScreen.route) }
        )
    }
}