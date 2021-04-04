package com.example.faircon.framework.presentation.ui.cooling

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.faircon.SettingPreferences
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.presentation.components.LineChart
import com.example.faircon.framework.presentation.theme.FairconTheme
import kotlinx.coroutines.delay

@Composable
fun CoolingDetailScreen(
    theme: SettingPreferences.Theme,
    isWifiAvailable: Boolean,
    scaffoldState: ScaffoldState,
    viewModel: CoolingViewModel
) {
    FairconTheme(
        theme = theme
    ) {

        val faircon by viewModel.faircon.collectAsState()

        val list by remember {
            mutableStateOf(mutableListOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f))
        }

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 25.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Card(shape = MaterialTheme.shapes.large) {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        yAxisValues = list,
                        lineColors = listOf(
                            MaterialTheme.colors.primary,
                            MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}