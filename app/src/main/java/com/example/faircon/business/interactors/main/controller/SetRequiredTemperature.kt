package com.example.faircon.business.interactors.main.controller

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.main.ControllerService
import com.example.faircon.framework.datasource.network.main.TemperatureRequest
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * - Send the network request to change the required temperature.
 * - [GenericResponse] is returned from the server.
 * - If request is successful, Controller dataStore values are updated.
 */
class SetRequiredTemperature(
    private val controllerService: ControllerService,
    private val controllerDataStore: ControllerDataStore,
    private val wiFiLiveData: WiFiLiveData
) {
    fun execute(
        temperature: Float,
        stateEvent: StateEvent
    ) = flow {

        if (wiFiLiveData.value != true) {
            emit(
                DataState.error<ControllerViewState>(
                    response = Response(
                        message = "Not Connected to FAIRCON",
                        uiType = UiType.SnackBar,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        } else {
            val apiResult = safeApiCall(Dispatchers.IO) {
                controllerService.setTemperature(TemperatureRequest(temperature))
            }

            emit(
                object : ApiResponseHandler<ControllerViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<ControllerViewState>? {

                        if (resultObj.response == SUCCESS) {
                            controllerDataStore.updateTemperature(temperature)
                        }

                        return DataState.data(
                            data = null,
                            response = Response(
                                message = resultObj.response,
                                uiType = UiType.None,
                                messageType = MessageType.Info
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    companion object {
        const val SUCCESS = "SUCCESS"
    }
}