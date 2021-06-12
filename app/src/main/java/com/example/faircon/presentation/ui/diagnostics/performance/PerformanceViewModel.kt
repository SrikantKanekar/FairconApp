package com.example.faircon.presentation.ui.diagnostics.performance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faircon.network.webSocket.WebSocket
import com.example.faircon.presentation.ui.diagnostics.performance.PerformanceTestStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformanceViewModel @Inject constructor(
    private var webSocket: WebSocket
) : ViewModel() {

    var state by mutableStateOf(NONE)

    fun startTest() {
        //webSocket.startTest()
        viewModelScope.launch {
            state = STARTING
            delay(2000)
            state = MOTOR_TEST
            delay(4000)
            state = TEC_TEST
            delay(3000)
            state = OTHER_TEST
            delay(3000)
            state = EVALUATING
            delay(4000)
            state = COMPLETED
        }
    }

    fun stopTest() {
        //webSocket.stopTest()
        state = NONE
    }

}

enum class PerformanceTestStates {
    NONE, STARTING, MOTOR_TEST, TEC_TEST, OTHER_TEST, EVALUATING, COMPLETED
}
