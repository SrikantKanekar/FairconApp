package com.example.faircon.framework.presentation.ui.main.account

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.framework.presentation.components.*
import com.example.faircon.framework.presentation.components.image.MyCircularImage

@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: AccountViewModel
) {
    val viewState by viewModel.viewState.observeAsState()

    val email = viewState?.accountProperties?.email

    val username = viewState?.accountProperties?.username

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {


            IconButton(
                modifier = Modifier.padding(top = 16.dp).align(Alignment.End),
                onClick = { navController.navigate(com.example.faircon.framework.presentation.navigation.AccountScreen.UpdateAccount.route) }
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
                    navController.navigate(com.example.faircon.framework.presentation.navigation.AccountScreen.ResetPassword.route)
                }
            )

            MyButton(
                modifier = Modifier.padding(bottom = 45.dp).padding(top = 15.dp),
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
            modifier = Modifier.padding(10.dp).preferredSize(40.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        Column {
            ProfileDetailText(text = field)
            ProfileDetailText(text = value)
        }
    }
}