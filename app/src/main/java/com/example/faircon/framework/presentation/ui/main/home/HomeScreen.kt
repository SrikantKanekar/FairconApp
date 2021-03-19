package com.example.faircon.framework.presentation.ui.main.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.business.domain.model.MODE
import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.framework.presentation.components.*
import com.example.faircon.framework.presentation.navigation.MainScreen
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent.SetModeEvent

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    theme: Int,
    isWiFiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    navAccountActivity: () -> Unit
) {
    val parameter = homeViewModel.homeFlow.collectAsState(initial = Parameter())

    FairconTheme(
        theme = theme,
        isWiFiAvailable = isWiFiAvailable,
        scaffoldState = scaffoldState,
        stateMessage = homeViewModel.stateMessage.value,
        removeStateMessage = { homeViewModel.removeStateMessage() }
    ) {

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
            bottomBar = {
                BottomAppBar(cutoutShape = CircleShape) {

                    IconButton(
                        onClick = navAccountActivity
                    ) {
                        MyIcon(imageVector = Icons.Default.Person)
                    }

                    IconButton(
                        onClick = {
                            navController.navigate(MainScreen.Settings.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        }
                    ) {
                        MyIcon(imageVector = Icons.Default.Settings)
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(MainScreen.Controller.route) }
                ) {
                    MyIcon(imageVector = Icons.Default.SettingsRemote)
                }
            },
            isFloatingActionButtonDocked = true
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(120.dp))

                ShowData(
                    name = Parameters.FAN_SPEED,
                    value = parameter.value.fanSpeed.toFloat(),
                    unit = "RPM",
                )

                ShowData(
                    name = Parameters.TEMPERATURE,
                    value = parameter.value.temperature,
                    unit = "C",
                )

                ShowData(
                    name = Parameters.TEC_VOLTAGE,
                    value = parameter.value.tecVoltage,
                    unit = "V",
                )

                HomeSwitch(
                    text = "Mode",
                    onCheckedChange = {
                        homeViewModel.setStateEvent(
                            SetModeEvent(
                                when (it) {
                                    true -> MODE.COOLING
                                    false -> MODE.ON
                                }
                            )
                        )
                    }
                )

                HomeSwitch(
                    text = "Connect",
                    onCheckedChange = { value ->
                        when (value) {
                            true -> homeViewModel.connectToFaircon()
                            false -> homeViewModel.disconnectFromFaircon()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ShowData(
    name: String,
    value: Float,
    unit: String
) {
    var progress: Float? = null
    when (name) {
        Parameters.FAN_SPEED -> {
            progress = (value - 300) / 100
        }
        Parameters.TEMPERATURE -> {
            progress = (value - 15) / 10
        }
        Parameters.TEC_VOLTAGE -> {
            progress = value / 12
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        MyOverlineText(text = name)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MyValueText(
                modifier = Modifier.width(80.dp),
                text = "$value $unit"
            )

            MyLinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                progress = progress!!
            )
        }
    }
}

@Composable
fun HomeSwitch(
    text: String,
    onCheckedChange: (Boolean) -> Unit
) {
    var mode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Switch(
            checked = mode,
            onCheckedChange = { value ->
                mode = value
                onCheckedChange(value)
            }
        )
    }
}

object Parameters {
    const val FAN_SPEED = "Fan Speed"
    const val TEMPERATURE = "Temperature"
    const val TEC_VOLTAGE = "Tec Voltage"
}