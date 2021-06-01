package com.example.faircon.network.fairconConnection

import com.example.faircon.network.response.GenericResponse
import com.example.faircon.network.services.FairconService
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Make sure to execute this on a background thread.
 */
@Singleton
class PingFaircon
@Inject
constructor(
    private val fairconService: FairconService
) {

    suspend fun execute(): Boolean {
        lateinit var apiCall: GenericResponse
        return try {
            withTimeout(500) {
                apiCall = fairconService.testConnection()
            }
            apiCall.response == "FAIRCON"
        } catch (throwable: Throwable) {
            false
        }
    }
}