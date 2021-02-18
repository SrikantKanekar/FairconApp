package com.example.faircon.framework.presentation.ui.auth.passwordReset

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.navigation.AuthScreen

@Composable
fun PasswordResetSuccessScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Password reset email sent",
            style = MaterialTheme.typography.h5,
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "We\\'ve emailed you instructions for setting your password. If an account exists\n" +
                    "        with the email you entered.\n" +
                    "        You should receive them shortly.If you don\\'t receive an email, please make sure you\\'ve entered the address you\n" +
                    "        registered with,\n" +
                    "        and check your spam folder.",
            style = MaterialTheme.typography.body1,
        )

        MyLinkTextButton(
            text = "Return",
            onClick = {
                navController.navigate(AuthScreen.LauncherScreen.route) {
                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            }
        )
    }
}