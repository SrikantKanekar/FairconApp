package com.example.faircon.framework.presentation.ui.main.home

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.main.home.HomeInteractors
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.presentation.ui.BaseApplication
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
    private val app: BaseApplication,
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
        // NA
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
            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    fun connectToFaircon() {
        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.apply {
            SSID = "\"FAIRCON\""
            preSharedKey = "\"12345678\""
        }
        val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.apply {
            isWifiEnabled = true
            val netId = addNetwork(wifiConfiguration)
            disconnect()
            enableNetwork(netId, true)
            reconnect()
        }
    }

    fun disconnectFromFaircon() {
        val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
    }
}