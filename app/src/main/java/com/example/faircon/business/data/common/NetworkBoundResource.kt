package com.example.faircon.business.data.common

import com.example.faircon.business.data.network.ApiResult.*
import com.example.faircon.business.data.cache.CacheResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.business.domain.util.printLogD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent,
    private val apiCall: suspend () -> NetworkObj?,
    private val cacheCall: suspend () -> CacheObj?
) {
    val result: Flow<DataState<ViewState>?> = flow {

        // ****** STEP 1: VIEW CACHE ******
        printLogD("NetworkBoundResource", "-------VIEW CACHE-------")
        emit(returnCache(markJobComplete = false))


        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        printLogD("NetworkBoundResource", "-------MAKE NETWORK CALL, SAVE RESULT TO CACHE-------")
        val apiResult = safeApiCall(dispatcher) { apiCall.invoke() }

        when (apiResult) {
            is GenericError -> {
                printLogD("NetworkBoundResource", "Code : ${apiResult.code}")
                emit(
                    DataState.error<ViewState>(
                        response = Response(
                            message = "${stateEvent.errorInfo()}\n\nReason: ${apiResult.errorMessage}",
                            uiType = UiType.None,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                )
            }

            is NetworkError -> {
                emit(
                    DataState.error<ViewState>(
                        response = Response(
                            message = "${stateEvent.errorInfo()}\n\nReason: $NETWORK_ERROR",
                            uiType = UiType.None,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                )
            }

            is Success -> {
                if (apiResult.value == null) {
                    emit(
                        DataState.error<ViewState>(
                            response = Response(
                                message = "${stateEvent.errorInfo()}\n\nReason: $UNKNOWN_ERROR",
                                uiType = UiType.None,
                                messageType = MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    )
                } else {
                    updateCache(apiResult.value as NetworkObj)
                }
            }
        }

        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
        printLogD("NetworkBoundResource", "-------VIEW CACHE and MARK JOB COMPLETED-------")
        emit(returnCache(markJobComplete = true))
    }

    private suspend fun returnCache(markJobComplete: Boolean): DataState<ViewState>? {

        val cacheResult = safeCacheCall(dispatcher) { cacheCall.invoke() }

        var jobCompleteMarker: StateEvent? = null
        if (markJobComplete) {
            jobCompleteMarker = stateEvent
        }

        return object : CacheResponseHandler<ViewState, CacheObj>(
            response = cacheResult,
            stateEvent = jobCompleteMarker
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState>? {
                return handleCacheSuccess(resultObj)
            }
        }.getResult()
    }

    abstract suspend fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState>?

    companion object {
        const val NETWORK_ERROR = "Network error"
        const val UNKNOWN_ERROR = "Unknown Error"
    }
}