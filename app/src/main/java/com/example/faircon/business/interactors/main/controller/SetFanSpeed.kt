package com.example.faircon.business.interactors.main.controller

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.model.Controller
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.main.ControllerService
import com.example.faircon.framework.datasource.network.main.FanSpeedRequest
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow

/**
 * - Send the network request to change the fan speed.
 * - [GenericResponse] is returned from the server.
 * - If request is successful, Controller dataStore values are updated.
 */
class SetFanSpeed(
    private val controllerService: ControllerService,
    private val controllerDataStore: ControllerDataStore,
    private val wiFiLiveData: WiFiLiveData
) {
    fun execute(
        fanSpeed: Int,
        stateEvent: StateEvent
    ) = flow {

        if (wiFiLiveData.value != true){
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
        }else{

            val apiResult = safeApiCall(IO){
                controllerService.setFanSpeed(FanSpeedRequest(fanSpeed))
            }

            emit(
                object : ApiResponseHandler<ControllerViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ){
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<ControllerViewState> {

                        if (resultObj.response == SUCCESS){
                            controllerDataStore.updateFanSpeed(fanSpeed)
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
                } .getResult()
            )
        }
    }

    companion object{
        const val SUCCESS = "SUCCESS"
    }
}