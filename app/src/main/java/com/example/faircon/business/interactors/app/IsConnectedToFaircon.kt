package com.example.faircon.business.interactors.app

import com.example.faircon.framework.datasource.network.response.GenericResponse
import com.example.faircon.framework.datasource.network.services.ConnectService
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
    private val homeService: ConnectService
) {

    suspend fun execute(): Boolean {
        var apiCall: GenericResponse?
        return try {
            withTimeout(500) {
                apiCall = homeService.testConnection()
            }
            apiCall?.response == "FAIRCON"
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> false
                is TimeoutCancellationException -> false
                else -> false
            }
        }
    }
}