package com.example.faircon.business.interactors.home

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.model.Mode
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.example.faircon.framework.datasource.network.services.HomeService
import com.example.faircon.framework.datasource.network.services.ModeRequest
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * - Send the network request to change the mode of the FAIRCON.
 * - [GenericResponse] is returned from the server.
 * - If request is successful, Home dataStore mode value is updated.
 */
class SetMode(
    private val homeService: HomeService,
    private val wiFiLiveData: WiFiLiveData
) {

    fun execute(
        mode: Mode,
        stateEvent: StateEvent
    ) = flow {

        if (wiFiLiveData.value != true) {
            emit(
                DataState.error<HomeViewState>(
                    response = Response(
                        message = "Not Connected to FAIRCON",
                        uiType = UiType.None,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        } else {

            val apiResult = safeApiCall(Dispatchers.IO) {
                homeService.setMode(ModeRequest(mode.ordinal))
            }

            emit(
                object : ApiResponseHandler<HomeViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<HomeViewState> {

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
}