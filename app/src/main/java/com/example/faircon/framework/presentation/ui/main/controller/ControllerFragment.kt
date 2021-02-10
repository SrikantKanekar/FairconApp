package com.example.faircon.framework.presentation.ui.main.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.faircon.framework.presentation.components.MyOutlinedText
import com.example.faircon.framework.presentation.components.MySlider
import com.example.faircon.framework.presentation.theme.FairconTheme

class ControllerFragment : Fragment() {

    val viewModel by viewModels<ControllerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FairconTheme(
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value
                ) {

                    val sliderValues = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .sliderValues

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(60.dp))

                        ShowSlider(
                            name = "Fan Speed",
                            initialPosition = sliderValues.fanSpeed,
                            valueRange = 300f..400f,
                            steps = 20,
                            onValueChangeEnd = { viewModel.setFanSpeed(it) }
                        )

                        ShowSlider(
                            name = "Temperature",
                            initialPosition = sliderValues.temperature,
                            valueRange = 15f..25f,
                            steps = 10,
                            onValueChangeEnd = { viewModel.setTemperature(it) }
                        )

                        ShowSlider(
                            name = "Tec Voltage",
                            initialPosition = sliderValues.tecVoltage,
                            valueRange = 0f..12f,
                            steps = 24,
                            onValueChangeEnd = { viewModel.setTecVoltage(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSlider(
    name: String,
    initialPosition: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeEnd: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        MyOutlinedText(text = name)

        MySlider(
            initialPosition = initialPosition,
            valueRange = valueRange,
            steps = steps,
            onValueChangeEnd = { onValueChangeEnd(it) }
        )
    }
}
