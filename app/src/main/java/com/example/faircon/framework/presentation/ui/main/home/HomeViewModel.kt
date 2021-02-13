package com.example.faircon.framework.presentation.ui.main.home

import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.flow.Flow

class HomeViewModel: BaseViewModel<HomeViewState>() {
    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleNewData(data: HomeViewState) {
        data.parameters?.let { parameters ->
            setParameters(parameters)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<HomeViewState>?> = when(stateEvent){
            is HomeStateEvent.GetParametersEvent->{
                TODO()
            }
            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    private fun setParameters(parameters: Parameter) {
        val update = getCurrentViewStateOrNew()
        update.parameters = parameters
        setViewState(update)
    }

}