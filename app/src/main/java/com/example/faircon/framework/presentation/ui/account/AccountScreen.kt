package com.example.faircon.framework.presentation.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.framework.presentation.components.MyButton
import com.example.faircon.framework.presentation.components.MyIcon
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.components.ProfileDetailText
import com.example.faircon.framework.presentation.components.image.MyCircularImage
import com.example.faircon.framework.presentation.navigation.AccountScreen

@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: AccountViewModel
) {
    val viewState = viewModel.viewState.collectAsState()
    val email = viewState.value.accountProperties?.email
    val username = viewState.value.accountProperties?.username

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                onClick = { navController.navigate(AccountScreen.UpdateAccount.route) }
            ) {
                MyIcon(imageVector = Icons.Default.Edit)
            }

            MyCircularImage(
                modifier = Modifier.padding(start = 20.dp),
                imageVector = Icons.Default.Person
            )

            Divider(modifier = Modifier.padding(vertical = 30.dp))

            if (email != null) {
                ProfileDetail(
                    field = "Email",
                    value = email,
                    imageVector = Icons.Default.Email
                )
            }

            if (username != null) {
                ProfileDetail(
                    modifier = Modifier.padding(top = 15.dp),
                    field = "Username",
                    value = username,
                    imageVector = Icons.Default.Person
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyLinkTextButton(
                text = "Change Password",
                onClick = {
                    navController.navigate(AccountScreen.ResetPassword.route)
                }
            )

            MyButton(
                modifier = Modifier
                    .padding(bottom = 45.dp)
                    .padding(top = 15.dp),
                text = "Logout",
                onClick = { viewModel.logout() }
            )
        }
    }
}

@Composable
fun ProfileDetail(
    modifier: Modifier = Modifier,
    field: String,
    value: String,
    imageVector: ImageVector
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(40.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        Column {
            ProfileDetailText(text = field)
            ProfileDetailText(text = value)
        }
    }
}