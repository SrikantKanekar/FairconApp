package com.example.faircon.framework.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun WiFiMonitor(
    isWiFiAvailable: Boolean,
){
    if(!isWiFiAvailable){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Not Connected to FAIRCON",
                fontSize = 12.sp,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}