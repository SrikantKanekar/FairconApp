package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.cache.CacheResponseHandler
import com.example.faircon.business.data.common.safeCacheCall
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.entity.AccountProperties
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.presentation.ui.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * Checks if user is logged in during app startUp.
 *
 *  - Check [EmailDataStore] for currently logged in user email
 *  - If No email found, user is navigated to login screen
 *  - If email is found, [AccountProperties] Database is scanned for that email.
 *  - If the user exists, then AuthToken Database is scanned corresponding to primary key and authToken is returned
 *  - If the user doesn't exist, user is navigated to login screen
 */
class CheckPreviousUser(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val emailDataStore: EmailDataStore
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>?> = flow {

        val email = emailDataStore.preferenceFlow.first()

        if (email.isNullOrBlank()) {
            emit(userNotFound(stateEvent))
        } else {

            val cacheResult = safeCacheCall(Dispatchers.IO) {
                accountPropertiesDao.searchByEmail(email)
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
                                        data = AuthViewState(authToken = authToken),
                                        response = Response(
                                            message = USER_FOUND,
                                            uiType = UiType.None,
                                            messageType = MessageType.Success
                                        ),
                                        stateEvent = stateEvent
                                    )
                                }
                            }
                        }
                        return userNotFound(stateEvent)
                    }
                }.getResult()
            )
        }
    }

    private fun userNotFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState> {
        return DataState.data(
            data = AuthViewState(previousUserCheck = true),
            response = Response(
                USER_NOT_FOUND,
                UiType.None,
                MessageType.Error
            ),
            stateEvent = stateEvent
        )
    }

    companion object {
        const val USER_FOUND = "previously authenticated user found"
        const val USER_NOT_FOUND = "No previously authenticated user found"
    }
}