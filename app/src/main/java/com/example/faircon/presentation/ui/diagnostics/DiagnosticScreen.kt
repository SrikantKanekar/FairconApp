package com.example.faircon.presentation.ui.diagnostics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiagnosticScreen(
    navigateToHealth: () -> Unit,
    navigateToPerformance: () -> Unit
) {

    Spacer(modifier = Modifier.height(50.dp))

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToHealth() }
                .padding(horizontal = 16.dp, vertical = 25.dp),
            text = "Health"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToPerformance() }
                .padding(horizontal = 16.dp, vertical = 25.dp),
            text = "Performance"
        )
    }
}
