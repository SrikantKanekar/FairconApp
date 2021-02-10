package com.example.faircon.framework.presentation.ui.main.controller

import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerStateEvent.*
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState.*
import kotlinx.coroutines.flow.Flow

class ControllerViewModel : BaseViewModel<ControllerViewState>() {

    override fun initNewViewState(): ControllerViewState {
        return ControllerViewState()
    }

    override fun handleNewData(data: ControllerViewState) {
        data.sliderValues.let { sliderValues ->
            setSliderValues(sliderValues)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ControllerViewState>?> = when (stateEvent) {
            is GetSliderValueEvent -> {
                TODO()
            }
            is SetFanSpeedEvent -> {
                TODO()
            }
            is SetTemperatureEvent -> {
                TODO()
            }
            is SetTecVoltageEvent -> {
                TODO()
            }
            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    private fun setSliderValues(sliderValues: SliderValues) {
        val update = getCurrentViewStateOrNew()
        update.sliderValues = sliderValues
        setViewState(update)
    }

    fun setFanSpeed(value: Float) {
        val update = getCurrentViewStateOrNew()
        update.sliderValues.fanSpeed = value
        setViewState(update)
    }

    fun setTemperature(value: Float) {
        val update = getCurrentViewStateOrNew()
        update.sliderValues.temperature = value
        setViewState(update)
    }

    fun setTecVoltage(value: Float) {
        val update = getCurrentViewStateOrNew()
        update.sliderValues.tecVoltage = value
        setViewState(update)
    }
}