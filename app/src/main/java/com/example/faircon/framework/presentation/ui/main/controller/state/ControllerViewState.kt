package com.example.faircon.framework.presentation.ui.main.controller.state

import com.example.spotifyclone.business.domain.state.ViewState

class ControllerViewState(
    var sliderValues : SliderValues = SliderValues()
): ViewState{
    data class SliderValues(
        var fanSpeed: Float = 0f,
        var temperature: Float = 0f,
        var tecVoltage: Float = 0f,
    )
}