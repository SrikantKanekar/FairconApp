package com.example.faircon.framework.presentation.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.model.WebSocketEvent
import com.example.faircon.business.domain.model.WebSocketMessage
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.home.HomeInteractors
import com.example.faircon.framework.datasource.network.mappers.FairconMapper
import com.example.faircon.framework.datasource.network.response.FairconResponse
import com.example.faircon.framework.datasource.network.webSocket.WebSocketInteractor
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.home.state.HomeStateEvent.ConnectToFairconEvent
import com.example.faircon.framework.presentation.ui.home.state.HomeStateEvent.DisconnectFromFairconEvent
import com.example.faircon.framework.presentation.ui.home.state.HomeViewState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val homeInteractors: HomeInteractors,
    private val webSocket: WebSocketInteractor,
    private val fairconMapper: FairconMapper
) : BaseViewModel<HomeViewState>() {

    init {
        webSocket.startSocket()
        viewModelScope.launch {
            webSocket.subscribe().collect { socketUpdate ->
                handleSocketUpdate(socketUpdate)
            }
        }
    }

    override fun initViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleNewData(data: HomeViewState) {
        data.serverConnected?.let { isConnected ->
            setViewState(viewState.value.copy(serverConnected = isConnected))
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<HomeViewState>?> = when (stateEvent) {
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
            val fairconResponse = Gson().fromJson(message, FairconResponse::class.java)
            val faircon = fairconMapper.mapToDomainModel(fairconResponse)
            setViewState(viewState.value.copy(faircon = faircon))
            Log.d("APP_DEBUG", "Raw : $message")
        }
        update.exception?.let { exception ->
            Log.d("APP_DEBUG", "socketUpdate exception : ${exception.message}")
            webSocket.reconnectSocket()
        }
    }

    fun setMode(mode: Mode) {
        val model = WebSocketMessage("mode", mode.ordinal)
        val message = Gson().toJson(model)
        webSocket.sendMessage(message)
    }

    override fun onCleared() {
        super.onCleared()
        webSocket.closeSocket()
    }
}