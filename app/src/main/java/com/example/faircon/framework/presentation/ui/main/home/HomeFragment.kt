package com.example.faircon.framework.presentation.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.framework.presentation.components.MyFab
import com.example.faircon.framework.presentation.components.MyLinearProgressIndicator
import com.example.faircon.framework.presentation.components.MyOverlineText
import com.example.faircon.framework.presentation.components.MyValueText
import com.example.faircon.framework.presentation.theme.FairconTheme

class HomeFragment :Fragment(){

    val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FairconTheme(
                    darkTheme = true,
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value
                ) {

                    Scaffold {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                            ) {

                                Spacer(modifier = Modifier.height(50.dp))

                                ShowData(
                                    name = "Fan Speed",
                                    value = 350f,
                                    unit = "RPM",
                                    progress = 0.5f
                                )

                                ShowData(
                                    name = "Temperature",
                                    value = 19f,
                                    unit = "C",
                                    progress = 0.4f
                                )

                                ShowData(
                                    name = "Tec Voltage",
                                    value = 8.5f,
                                    unit = "V",
                                    progress = 0.7f
                                )
                            }

                            MyFab(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                imageVector = Icons.Filled.SettingsRemote,
                                onClick = { findNavController().navigate(R.id.controllerFragment) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowData(
    name: String,
    value: Float,
    unit: String,
    progress: Float
) {
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
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                progress = progress
            )
        }
    }
}
