package com.example.faircon.framework.presentation.ui.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faircon.framework.presentation.components.MyIcon

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ){

        SwitchSetting(
            imageVector = Icons.Default.Person,
            isDark = viewModel.isDark,
            value = if (viewModel.isDark) "Dark" else "Light",
            onCheckedChange = { boolean ->
                viewModel.setTheme(boolean)
            }
        )
    }
}

@Composable
fun SwitchSetting(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    isDark: Boolean,
    value: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row{
            MyIcon(
                modifier = Modifier.padding(5.dp),
                imageVector = imageVector
            )

            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Text(
                    text = "Theme",
                    fontSize = 15.sp
                )
                Text(
                    text = value,
                    fontSize = 13.sp
                )
            }
        }

        Switch(
            modifier = Modifier.padding(5.dp),
            checked = isDark,
            onCheckedChange = onCheckedChange
        )
    }
}