package com.example.faircon.business.interactors.main.home

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.connectivity.WiFiLiveData
import com.example.faircon.framework.datasource.network.main.HomeService
import com.example.faircon.framework.datasource.network.main.ModeRequest
import com.example.faircon.framework.presentation.ui.main.controller.state.ControllerViewState
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
    private val homeDataStore: HomeDataStore,
    private val wiFiLiveData: WiFiLiveData
) {

    fun execute(
        mode: Int,
        stateEvent: StateEvent
    ) = flow {

        if (wiFiLiveData.value != true) {
            emit(
                DataState.error<HomeViewState>(
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
                homeService.setMode(ModeRequest(mode))
            }

            emit(
                object : ApiResponseHandler<HomeViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<HomeViewState>? {

                        if (resultObj.response == SUCCESS) {
                            homeDataStore.updateMode(mode)
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