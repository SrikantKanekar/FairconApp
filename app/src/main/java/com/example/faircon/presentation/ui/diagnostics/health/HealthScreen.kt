package com.example.faircon.presentation.ui.diagnostics.health

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.faircon.presentation.components.HealthChart

@Composable
fun HealthScreen() {

    val healthViewModel = hiltViewModel<HealthViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        HealthItem(
            number = 1,
            component = "Motor",
            lifeCompleted = 11021,
            lifeTotal = 20000,
            yValues = mutableListOf(
                99, 95, 93, 88, 84, 78, 76, 73, 70, 68,
                64, 60, 55, 49, 45
            )
        )
        HealthItem(
            number = 2,
            component = "Tec",
            lifeCompleted = 9047,
            lifeTotal = 20000,
            yValues = mutableListOf(
                99, 98, 93, 88, 86, 78, 76, 76, 70, 69,
                68, 67, 63, 58, 54
            )
        )
    }
}

@Composable
fun HealthItem(
    number: Int,
    component: String,
    lifeCompleted: Int,
    lifeTotal: Int,
    yValues: List<Int>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {

        val progress = lifeCompleted.toFloat() / lifeTotal.toFloat()
        val expectedLife = lifeTotal - lifeCompleted

        Box(
            Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(text = number.toString())
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = component,
                        fontWeight = FontWeight.Bold
                    )
                }
                HealthChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(10.dp),
                    yValues = yValues

                )
                Column(
                    Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "${progress * 100}% Completed",
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = "Expected life : $expectedLife hrs",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}
