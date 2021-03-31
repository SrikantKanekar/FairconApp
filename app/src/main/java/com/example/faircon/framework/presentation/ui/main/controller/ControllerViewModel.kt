package com.example.faircon.framework.presentation.ui.main.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.model.Controller
import com.example.faircon.business.domain.model.Faircon
import com.example.faircon.business.domain.model.WebSocketEvent
import com.example.faircon.business.domain.model.WebSocketMessage
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.mappers.FairconMapper
import com.example.faircon.framework.datasource.network.response.FairconResponse
import com.example.faircon.framework.datasource.network.webSocket.WebSocketInteractor
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControllerViewModel @Inject constructor(
    private val webSocket: WebSocketInteractor,
    private val fairconMapper: FairconMapper,
    private val controllerDataStore: ControllerDataStore
) : ViewModel() {

    val controller = controllerDataStore.controllerFlow
    var controllerValue = Controller()

    init {
        webSocket.startSocket()
        viewModelScope.launch {
            webSocket.subscribe().collect { socketUpdate ->
                handleSocketUpdate(socketUpdate)
            }
        }
    }

    private fun handleSocketUpdate(update: WebSocketEvent) {
        update.isConnected?.let {

        }
        update.message?.let { message ->
            val fairconResponse = Gson().fromJson(message, FairconResponse::class.java)
            val faircon = fairconMapper.mapToDomainModel(fairconResponse)
            updateController(faircon)
            Log.d("APP_DEBUG", "Raw : $message")
        }
        update.exception?.let { exception ->
            Log.d("APP_DEBUG", "socketUpdate exception : ${exception.message}")
            webSocket.reconnectSocket()
        }
    }

    private fun updateController(faircon: Faircon) {
        if (controllerValue != faircon.controller){
            printLogD("", "Updated controller datastore")
            controllerValue = faircon.controller
            controllerDataStore.updateController(faircon.controller)
        }
    }

    fun updateFanSpeed(value: Int){
        val model = WebSocketMessage("fanSpeed", value)
        val message = Gson().toJson(model)
        webSocket.sendMessage(message)
    }

    fun updateTemperature(value: Float){
        val model = WebSocketMessage("temperature", value)
        val message = Gson().toJson(model)
        webSocket.sendMessage(message)
    }

    fun updateTecVoltage(value: Float){
        val model = WebSocketMessage("tecVoltage", value)
        val message = Gson().toJson(model)
        webSocket.sendMessage(message)
    }
}
