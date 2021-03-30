package com.example.faircon.framework.presentation.ui.main.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.model.WebSocketEvent
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.home.HomeInteractors
import com.example.faircon.framework.datasource.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.mappers.ParameterMapper
import com.example.faircon.framework.datasource.network.response.ParameterResponse
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.home.state.HomeStateEvent.*
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val homeInteractors: HomeInteractors,
    private val fairconConnection: WiFiLiveData,
    private val parameterMapper: ParameterMapper
) : BaseViewModel<HomeViewState>() {

    init {
        startSocket()
        viewModelScope.launch {
            homeInteractors.webSocket.subscribe().collect { socketUpdate ->
                handleSocketUpdate(socketUpdate)
            }
            // can be removed
            while (true) {
                if (fairconConnection.value == true) {
                    if (viewState.value.syncedController != true) {
                        setStateEvent(SyncControllerEvent)
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
        data.serverConnected?.let { isConnected ->
            setViewState(viewState.value.copy(serverConnected = isConnected))
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<HomeViewState>?> = when (stateEvent) {
            is SyncControllerEvent -> {
                homeInteractors.syncController.execute(stateEvent)
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

    private fun handleSocketUpdate(update: WebSocketEvent) {
        update.isConnected?.let { boolean ->
            setViewState(viewState.value.copy(webSocketConnected = boolean))
        }
        update.message?.let { message ->
            val parameterResponse = Gson().fromJson(message, ParameterResponse::class.java)
            val parameter = parameterMapper.mapToDomainModel(parameterResponse)
            setViewState(viewState.value.copy(parameter = parameter))
//            Log.d("APP_DEBUG", "Raw : $message")
        }
        update.exception?.let { exception ->
            Log.d("APP_DEBUG", "socketUpdate exception : ${exception.message}")
            reconnectSocket()
        }
    }

    private fun startSocket() {
        homeInteractors.webSocket.startSocket()
    }

    fun sendMessage() {
        homeInteractors.webSocket.sendMessage()
    }

    private fun reconnectSocket(){
        homeInteractors.webSocket.reconnectSocket()
    }

    override fun onCleared() {
        super.onCleared()
        homeInteractors.webSocket.closeSocket()
    }
}