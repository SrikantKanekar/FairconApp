package com.example.faircon.framework.presentation.ui.main.home

import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.home.HomeInteractors
import com.example.faircon.framework.datasource.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
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
    private val fairconConnection: WiFiLiveData,
    homeDataStore: HomeDataStore
) : BaseViewModel<HomeViewState>() {

    val homeFlow = homeDataStore.homeFlow


    init {
        viewModelScope.launch {
            while (true) {
                if (fairconConnection.value == true) {
                    if (viewState.value.syncedController != true) {
                        setStateEvent(SyncControllerEvent)
                    }
                    setStateEvent(GetParametersEvent)
                } else {
                    if (viewState.value.clearedHomeDatastore != true) {
                        homeDataStore.clear()
                        setViewState(viewState.value.copy(clearedHomeDatastore = true))
                    }
                }
                delay(5000)
            }
        }
    }

    override fun initViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleNewData(data: HomeViewState) {
        data.syncedController?.let { synced ->
            setViewState(viewState.value.copy(syncedController = synced))
        }
        data.connected?.let { isConnected ->
            setViewState(viewState.value.copy(connected = isConnected))
        }
        data.clearedHomeDatastore?.let { cleared ->
            setViewState(viewState.value.copy(clearedHomeDatastore = cleared))
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