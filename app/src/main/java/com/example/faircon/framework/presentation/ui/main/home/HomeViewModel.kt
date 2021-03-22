package com.example.faircon.framework.presentation.ui.main.home

import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.main.home.HomeInteractors
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent.*
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val homeInteractors: HomeInteractors,
    private val wiFiLiveData: WiFiLiveData,
    homeDataStore: HomeDataStore
) : BaseViewModel<HomeViewState>() {

    val homeFlow = homeDataStore.homeFlow

    init {
        viewModelScope.launch {
            if (wiFiLiveData.value == true) {
                setStateEvent(SyncControllerEvent)
            }
            while (true) {
                if (wiFiLiveData.value == true) {
                    setStateEvent(GetParametersEvent)
                }
                delay(5000)
            }
        }
    }

    override fun initViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleNewData(data: HomeViewState) {
        data.connected?.let { isConnected ->
            setViewState(viewState.value.copy(connected = isConnected))
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<HomeViewState>?> = when (stateEvent) {
            is SyncControllerEvent -> {
                homeInteractors.syncController.execute(stateEvent)
            }
            is GetParametersEvent -> {
                homeInteractors.getParameters.execute(stateEvent)
            }
            is SetModeEvent -> {
                homeInteractors.setMode.execute(stateEvent.mode, stateEvent)
            }
            is ConnectToFairconEvent -> {
                homeInteractors.connectToFaircon.execute(stateEvent)
            }
            is DisconnectFromFairconEvent -> {
                homeInteractors.disconnectFromFaircon.execute(stateEvent)
            }
            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }
}