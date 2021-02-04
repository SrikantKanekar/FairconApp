package com.example.faircon.business.data.common

import com.example.faircon.business.data.cache.CacheResult
import com.example.faircon.business.data.common.ERRORS.CACHE_ERROR_TIMEOUT
import com.example.faircon.business.data.common.ERRORS.CACHE_ERROR_UNKNOWN
import com.example.faircon.business.data.common.ERRORS.ERROR_UNKNOWN
import com.example.faircon.business.data.common.ERRORS.NETWORK_ERROR_TIMEOUT
import com.example.faircon.business.data.common.ERRORS.NETWORK_ERROR_UNKNOWN
import com.example.faircon.business.data.common.TIMEOUTS.CACHE_TIMEOUT
import com.example.faircon.business.data.common.TIMEOUTS.NETWORK_TIMEOUT
import com.example.faircon.business.data.network.ApiResult
import com.example.faircon.business.data.network.ApiResult.*
import com.example.faircon.business.domain.util.printLogD
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                printLogD("safeApiCall", "Executing Network Request")
                Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            printLogD("safeApiCall", "Error in executing Network Request")
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408
                    GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(CACHE_TIMEOUT) {
                printLogD("safeApiCall", "Executing Cache Request")
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            printLogD("safeApiCall", "Error in executing Cache Request")
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}

object TIMEOUTS {
    const val NETWORK_TIMEOUT = 6000L
    const val CACHE_TIMEOUT = 2000L
}

object ERRORS {
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"

    const val CACHE_ERROR_TIMEOUT = "Cache timeout"
    const val CACHE_ERROR_UNKNOWN = "Unknown cache error"

    const val ERROR_UNKNOWN = "Unknown error"
}