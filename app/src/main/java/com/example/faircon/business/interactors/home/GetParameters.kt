package com.example.faircon.business.interactors.home

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.dataStore.HomeDataStore
import com.example.faircon.framework.datasource.network.mappers.ParameterMapper
import com.example.faircon.framework.datasource.network.response.ParameterResponse
import com.example.faircon.framework.datasource.network.services.HomeService
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * Network request is made to nodeMcu after a small interval to update home screen values.
 */
class GetParameters(
    private val homeService: HomeService,
    private val homeDataStore: HomeDataStore,
    private val parameterMapper: ParameterMapper
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            homeService.getParameters()
        }

        emit(
            object : ApiResponseHandler<HomeViewState, ParameterResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: ParameterResponse): DataState<HomeViewState> {

                    val parameter = parameterMapper.mapToDomainModel(resultObj)
                    homeDataStore.updateParameters(parameter)

                    return DataState.data(
                        data = HomeViewState(clearedHomeDatastore = false),
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