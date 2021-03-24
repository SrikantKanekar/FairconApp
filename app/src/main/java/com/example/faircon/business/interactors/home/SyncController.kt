package com.example.faircon.business.interactors.home

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.model.Controller
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.ControllerDataStore
import com.example.faircon.framework.datasource.network.services.ControllerService
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * This Network request is made during the App startUp to get Controller values
 * from the NodeMcu. The values are synced with [ControllerDataStore] values.
 */
class SyncController(
    private val controllerService: ControllerService,
    private val controllerDataStore: ControllerDataStore
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            controllerService.getController()
        }

        emit(
            object : ApiResponseHandler<HomeViewState, Controller>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Controller): DataState<HomeViewState> {

                    controllerDataStore.updateController(resultObj)

                    return DataState.data(
                        data = HomeViewState(syncedController = true),
                        response = Response(
                            message = "Controller values Synced",
                            uiType = UiType.None,
                            messageType = MessageType.Success
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }
}