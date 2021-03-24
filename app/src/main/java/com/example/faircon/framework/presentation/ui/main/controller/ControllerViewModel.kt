package com.example.faircon.framework.presentation.ui.main.controller

import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.controller.ControllerInteractors
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerStateEvent.*
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ControllerViewModel @Inject constructor(
    private val controllerInteractors: ControllerInteractors,
    controllerDataStore: ControllerDataStore
) : BaseViewModel<ControllerViewState>() {

    val controller = controllerDataStore.controllerFlow

    override fun initViewState(): ControllerViewState {
        return ControllerViewState()
    }

    override fun handleNewData(data: ControllerViewState) {
        // NA
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ControllerViewState>?> =

            when (stateEvent) {

                is SetFanSpeedEvent -> {
                    controllerInteractors.setFanSpeed.execute(
                        fanSpeed = stateEvent.fanSpeed,
                        stateEvent = stateEvent
                    )
                }
                is SetTemperatureEvent -> {
                    controllerInteractors.setRequiredTemperature.execute(
                        temperature = stateEvent.temperature,
                        stateEvent = stateEvent
                    )
                }
                is SetTecVoltageEvent -> {
                    controllerInteractors.setTecVoltage.execute(
                        voltage = stateEvent.voltage,
                        stateEvent = stateEvent
                    )
                }
                else -> emitInvalidStateEvent(stateEvent)
            }
        launchJob(stateEvent, job)
    }
}
