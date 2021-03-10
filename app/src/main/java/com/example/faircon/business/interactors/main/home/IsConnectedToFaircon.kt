package com.example.faircon.business.interactors.main.home

import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.HomeService
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Send a ping to FAIRCON. Returns a Success message.
 * If successful, that means we are connected to FAIRCON.
 *
 * Make sure to execute this on a background thread.
 */
@Singleton
class IsConnectedToFaircon
@Inject
constructor(
    private val homeService: HomeService
) {

    suspend fun execute(): Boolean {
        var apiCall: GenericResponse?
        return try {
            withTimeout(500) {
                apiCall = homeService.testConnection()
            }
            apiCall?.response == "SUCCESS"
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> false
                is TimeoutCancellationException -> false
                else -> false
            }
        }
    }
}