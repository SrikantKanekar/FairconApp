package com.example.faircon.business.interactors.controller

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.example.faircon.framework.datasource.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.services.ControllerService
import com.example.faircon.framework.datasource.network.services.TecVoltageRequest
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * - Send the network request to change the Tec voltage.
 * - [GenericResponse] is returned from the server.
 * - If request is successful, Controller dataStore values are updated.
 */
class SetTecVoltage(
    private val controllerService: ControllerService,
    private val controllerDataStore: ControllerDataStore,
    private val wiFiLiveData: WiFiLiveData
) {
    fun execute(
        voltage: Float,
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
                controllerService.setTecVoltage(TecVoltageRequest(voltage))
            }

            emit(
                object : ApiResponseHandler<ControllerViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<ControllerViewState>? {

                        if (resultObj.response == SUCCESS) {
                            controllerDataStore.updateTecVoltage(voltage)
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