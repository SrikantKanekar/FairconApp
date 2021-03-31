package com.example.faircon.framework.presentation.ui.main.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.SettingPreferences.Theme
import com.example.faircon.business.domain.model.Faircon
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.model.Mode.*
import com.example.faircon.framework.presentation.components.*
import com.example.faircon.framework.presentation.navigation.MainScreen
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent.ConnectToFairconEvent
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent.DisconnectFromFairconEvent

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    theme: Theme,
    isWiFiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    navAccountActivity: () -> Unit
) {
    val viewState = homeViewModel.viewState.collectAsState()
    val faircon = viewState.value.faircon ?: Faircon()
    val parameter = faircon.parameter

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
                        onClick = { navController.navigate(MainScreen.Settings.route) }
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
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(40.dp),
                    text = faircon.status.name
                )

                ShowParameter(
                    name = "Fan Speed",
                    unit = "RPM",
                    progress = parameter.fanSpeed.toFloat(),
                    valueRange = 300F..400F
                )

                ShowParameter(
                    name = "Room Temperature",
                    progress = parameter.roomTemperature,
                    unit = "C",
                    valueRange = 15F..25F
                )

                ShowParameter(
                    name = "Tec Voltage",
                    progress = parameter.tecVoltage,
                    unit = "V",
                    valueRange = 0F..12F
                )

                ShowParameter(
                    name = "Power Consumption",
                    progress = parameter.powerConsumption.toFloat(),
                    unit = "Kwh",
                    valueRange = 0F..1000F
                )

                ShowParameter(
                    name = "Heat Expelling",
                    progress = parameter.heatExpelling.toFloat(),
                    unit = "W",
                    valueRange = 0F..500F
                )

                ShowParameter(
                    name = "Tec Temperature",
                    progress = parameter.tecTemperature,
                    unit = "C",
                    valueRange = 25F..120F
                )

                ModeButtons(
                    mode = faircon.mode,
                    setMode = { homeViewModel.setMode(it) }
                )

                ConnectionButtons(
                    connected = viewState.value.serverConnected ?: false,
                    connect = {
                        homeViewModel.setStateEvent(ConnectToFairconEvent)
                    },
                    disConnect = {
                        homeViewModel.setStateEvent(DisconnectFromFairconEvent)
                    }
                )
            }
        }
    }
}

@Composable
fun ShowParameter(
    name: String,
    unit: String,
    progress: Float,
    valueRange: ClosedFloatingPointRange<Float>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        MyOverlineText(text = name)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            MyValueText(
                modifier = Modifier.width(80.dp),
                text = "$progress $unit"
            )

            MyLinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                progress = progress,
                valueRange = valueRange
            )
        }
    }
}

@Composable
fun ModeButtons(
    mode: Mode,
    setMode: (Mode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SelectableButton(
            isSelected = mode == IDLE,
            text = "Idle",
            onClick = { setMode(IDLE) }
        )

        SelectableButton(
            isSelected = mode == FAN,
            text = "Fan",
            onClick = { setMode(FAN) }
        )

        SelectableButton(
            isSelected = mode == COOLING,
            text = "Cooling",
            onClick = { setMode(COOLING) }
        )

        SelectableButton(
            isSelected = mode == HEATING,
            text = "Heating",
            onClick = { setMode(HEATING) }
        )
    }
}

@Composable
fun ConnectionButtons(
    connected: Boolean,
    connect: () -> Unit,
    disConnect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(bottom = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = connect, enabled = !connected) {
            Text(text = "Connect")
        }

        Button(onClick = disConnect, enabled = connected) {
            Text(text = "DisConnect")
        }
    }
}