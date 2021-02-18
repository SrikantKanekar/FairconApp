package com.example.faircon.business.data.network

import com.example.faircon.business.data.network.ApiResult.*
import com.example.faircon.business.domain.state.*
import com.example.faircon.business.domain.util.printLogD

abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n${response.errorMessage.toString()}",
                        uiType = UiType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is NetworkError -> {
                DataState.error(
                    response = Response(
                        message = NETWORK_ERROR,
                        uiType = UiType.SnackBar,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = NETWORK_DATA_NULL,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?

    companion object{
        const val NETWORK_ERROR = "Network error"
        const val NETWORK_DATA_NULL = "Network data is null"
    }
}