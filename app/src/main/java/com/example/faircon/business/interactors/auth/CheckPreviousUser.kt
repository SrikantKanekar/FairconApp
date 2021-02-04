package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.cache.CacheResponseHandler
import com.example.faircon.business.data.common.safeCacheCall
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.auth.AuthTokenDao
import com.example.faircon.framework.datasource.cache.main.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.preference.MyPreferences
import com.example.faircon.framework.presentation.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckPreviousUser(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val myPreferences: MyPreferences
) {

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>?> = flow {
        val previousAuthUserEmail = myPreferences.getAuthenticatedUser()

        if (previousAuthUserEmail.isNullOrBlank()) {
            emit(returnNoTokenFound(stateEvent))
        } else {
            val cacheResult = safeCacheCall(Dispatchers.IO) {
                accountPropertiesDao.searchByEmail(previousAuthUserEmail)
            }
            emit(
                object : CacheResponseHandler<AuthViewState, AccountProperties>(
                    response = cacheResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: AccountProperties): DataState<AuthViewState> {

                        if (resultObj.pk > -1) {
                            authTokenDao.searchByPk(resultObj.pk)?.let { authToken ->
                                if (authToken.token != null) {
                                    return DataState.data(
                                        data = AuthViewState(
                                            authToken = authToken
                                        ),
                                        response = null,
                                        stateEvent = stateEvent
                                    )
                                }
                            }
                        }
                        return DataState.error(
                            response = Response(
                                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                UIComponentType.None,
                                MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        }
    }

    private fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState> {

        return DataState.error(
            response = Response(
                RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                UIComponentType.None,
                MessageType.Error
            ),
            stateEvent = stateEvent
        )
    }

    companion object{
        const val RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE = "Done checking for previously authenticated user."
    }
}