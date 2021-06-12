package com.example.faircon.presentation.ui.diagnostics.performance

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faircon.presentation.components.PerformanceTestButton
import com.example.faircon.presentation.theme.connected
import com.example.faircon.presentation.ui.diagnostics.performance.PerformanceTestStates.COMPLETED
import com.example.faircon.presentation.ui.diagnostics.performance.PerformanceTestStates.NONE

@Composable
fun PerformanceScreen() {

    val viewModel = hiltViewModel<PerformanceViewModel>()

    if (viewModel.state == COMPLETED) {

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.height(30.dp))

            PerformanceResult(
                number = "1",
                name = "Tec",
                performance = "78%",
                idealResult = {
                    ResultsText(text = "Temp Diff : 58℃")
                    ResultsText(text = "Current : 6A")
                    ResultsText(text = "Power : 150Kwh")
                },
                actualResult = {
                    ResultsText(text = "Temp Diff : 70℃")
                    ResultsText(text = "Current : 4.2A")
                    ResultsText(text = "Power : 114Kwh")
                }
            )
            PerformanceResult(
                number = "2",
                name = "Motor",
                performance = "94%",
                idealResult = {
                    ResultsText(text = "Max Speed : 300Rpm")
                    ResultsText(text = "Current : 2A")
                    ResultsText(text = "Power : 45Kwh")
                },
                actualResult = {
                    ResultsText(text = "Max Speed : 289Rpm")
                    ResultsText(text = "Current : 1.9A")
                    ResultsText(text = "Power : 43Kwh")
                }
            )
            PerformanceResult(
                number = "3",
                name = "Controller",
                performance = "100%",
                idealResult = {
                    ResultsText(text = "Working Pins : 40")
                    ResultsText(text = "WiFi Strength : Full")
                    ResultsText(text = "Current : 500mA")
                },
                actualResult = {
                    ResultsText(text = "Working Pins : 40")
                    ResultsText(text = "WiFi Strength : Full")
                    ResultsText(text = "Current : 480mA")
                }
            )
            PerformanceResult(
                number = "4",
                name = "Sensors",
                performance = "96%",
                idealResult = {
                    ResultsText(text = "Fluctuations : 0V")
                },
                actualResult = {
                    ResultsText(text = "Fluctuations : 4mV")
                }
            )
            PerformanceResult(
                number = "5",
                name = "SMPS",
                performance = "88%",
                idealResult = {
                    ResultsText(text = "Current : 12A")
                    ResultsText(text = "Voltage : 36V")
                    ResultsText(text = "Power : 432Kwh")
                },
                actualResult = {
                    ResultsText(text = "Current : 11.6A")
                    ResultsText(text = "Voltage : 35.8V")
                    ResultsText(text = "Power : 415Kwh")
                }
            )
        }
    } else {
        PerformanceTestButton(
            modifier = Modifier.fillMaxSize(),
            currentState = viewModel.state,
            startTest = { if (viewModel.state == NONE) viewModel.startTest() }
        )
    }
}

@Composable
fun PerformanceResult(
    number: String,
    name: String,
    performance: String,
    idealResult: @Composable ColumnScope.() -> Unit,
    actualResult: @Composable ColumnScope.() -> Unit,
) {
    var collapsed by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp).padding(bottom = 12.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.width(160.dp)) {
                BoldText(text = number)
                Text(
                    modifier = Modifier.padding(start = 40.dp),
                    text = name,
                    fontWeight = FontWeight.Bold
                )
            }
            BoldText(text = performance)
            Icon(
                modifier = Modifier
                    .padding(15.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { collapsed = !collapsed }
                    ),
                imageVector = if (collapsed) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        if (collapsed) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 8.dp),
                        text = "Ideal",
                        color = connected,
                        fontWeight = FontWeight.Bold
                    )
                    idealResult()
                }
                Column {
                    Text(
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 8.dp),
                        text = "Actual",
                        color = connected,
                        fontWeight = FontWeight.Bold
                    )
                    actualResult()
                }
            }
        }
    }
}

@Composable
fun ResultsText(
    text: String
) {
    Text(
        modifier = Modifier,
        text = text,
        style = MaterialTheme.typography.caption
    )
}

@Composable
fun BoldText(
    text: String
) {
    Text(text = text, fontWeight = FontWeight.Bold)
}
