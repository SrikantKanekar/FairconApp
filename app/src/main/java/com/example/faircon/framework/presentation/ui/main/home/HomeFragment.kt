package com.example.faircon.framework.presentation.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.framework.presentation.components.MyFab
import com.example.faircon.framework.presentation.components.MyLinearProgressIndicator
import com.example.faircon.framework.presentation.components.MyOutlinedText
import com.example.faircon.framework.presentation.theme.FairconTheme

class HomeFragment :Fragment(R.layout.fragment_home){

    val viewModel by viewModels<HomeViewModel>()

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

                    Scaffold {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
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
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        MyOutlinedText(text = name)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyOutlinedText(
                modifier = Modifier.fillMaxWidth(),
                text = "$value $unit",
                fontSize = 15.sp
            )
            MyLinearProgressIndicator(
                modifier = Modifier,
                progress = progress
            )
        }
    }
}
