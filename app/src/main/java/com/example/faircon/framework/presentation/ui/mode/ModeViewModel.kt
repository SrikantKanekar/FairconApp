package com.example.faircon.framework.presentation.ui.mode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.faircon.business.domain.model.Mode

class ModeViewModel : ViewModel() {

    var currentMode by mutableStateOf(Mode.OFF)
    var navigate by mutableStateOf(false)

    fun updateMode(mode: Mode) {
        currentMode = mode
        navigate = true
    }
}