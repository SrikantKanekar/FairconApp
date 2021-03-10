package com.example.faircon.business.interactors.main.home

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.main.HomeService
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * Network request is made to nodeMcu after a small interval to update home screen values.
 */
class GetParameters(
    private val homeService: HomeService,
    private val homeDataStore: HomeDataStore
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            homeService.getParameters()
        }

        emit(
            object : ApiResponseHandler<HomeViewState, Parameter>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Parameter): DataState<HomeViewState> {

                    homeDataStore.updateParameters(resultObj)

                    return DataState.data(
                        data = null,
                        response = Response(
                            message = "Controller values updated",
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