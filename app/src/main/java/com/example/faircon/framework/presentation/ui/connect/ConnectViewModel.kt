package com.example.faircon.framework.presentation.ui.connect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.connect.ConnectInteractors
import com.example.faircon.framework.datasource.connectivity.FairconConnection
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.connect.ConnectViewModel.ConnectionState.*
import com.example.faircon.framework.presentation.ui.connect.state.ConnectStateEvent.ConnectToFairconEvent
import com.example.faircon.framework.presentation.ui.connect.state.ConnectStateEvent.DisconnectFromFairconEvent
import com.example.faircon.framework.presentation.ui.connect.state.ConnectViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel
@Inject
constructor(
    private val connectInteractors: ConnectInteractors,
    private val fairconConnection: FairconConnection
) : BaseViewModel<ConnectViewState>() {

    var initialCheck by mutableStateOf(false)
    var navigateToModeScreen by mutableStateOf(false)
    var connectionState by mutableStateOf(CONNECT)

    init {
        viewModelScope.launch {
            fairconConnection.available.collect { connection ->
                connection?.let {
                    connectionState = when(connection) {
                        true -> CONNECTED
                        false -> CONNECT
                    }
                    if (!initialCheck){
                        navigateToModeScreen = connection
                        initialCheck = true
                    }
                }
            }
        }
    }

    override fun initViewState(): ConnectViewState {
        return ConnectViewState()
    }

    override fun handleNewData(data: ConnectViewState) {
        //NA
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<ConnectViewState>?> = when (stateEvent) {
            is ConnectToFairconEvent -> {
                connectInteractors.connectToFaircon.execute(stateEvent)
            }
            is DisconnectFromFairconEvent -> {
                connectInteractors.disconnectFromFaircon.execute(stateEvent)
            }
            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    fun connect(){
        if (connectionState == CONNECT || connectionState == RETRY){
            connectionState = CONNECTING
            setStateEvent(ConnectToFairconEvent)
            viewModelScope.launch {
                try {
                    withTimeout(5000){
                        while (connectionState == CONNECTING){
                            delay(100)
                        }
                    }
                } catch (e: TimeoutCancellationException){
                    connectionState = RETRY
                    setStateEvent(DisconnectFromFairconEvent)
                }
            }
        }
    }

    enum class ConnectionState{
        CONNECT, CONNECTING, CONNECTED, RETRY
    }
}