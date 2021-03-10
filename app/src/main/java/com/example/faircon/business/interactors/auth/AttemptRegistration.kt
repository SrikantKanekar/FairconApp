package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.business.interactors.auth.AttemptLogin.Companion.ERROR_SAVE_AUTH_TOKEN
import com.example.faircon.business.interactors.auth.AttemptLogin.Companion.GENERIC_AUTH_ERROR
import com.example.faircon.business.interactors.auth.AttemptRegistration.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import com.example.faircon.business.interactors.auth.AttemptRegistration.Companion.ERROR_SAVE_AUTH_TOKEN
import com.example.faircon.business.interactors.auth.AttemptRegistration.Companion.GENERIC_AUTH_ERROR
import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
import com.example.faircon.framework.datasource.cache.authToken.AuthTokenDao
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.datasource.network.auth.AuthService
import com.example.faircon.framework.datasource.network.auth.response.RegistrationResponse
import com.example.faircon.framework.presentation.ui.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * - Attempt to resister the user
 * - make registration attempt
 * - If the registration credentials are invalid, [GENERIC_AUTH_ERROR] string is returned from the server and error is shown
 * - If the registration credentials are correct, accountProperties are inserted into [AccountProperties] database
 * - If -1 is returned, [ERROR_SAVE_ACCOUNT_PROPERTIES] is shown to user
 * - Similarly, pk and token is inserted into [AuthToken] database
 * - If -1 is returned, [ERROR_SAVE_AUTH_TOKEN] is shown to user
 * - email is stored in [EmailDataStore]
 * - [AuthToken] is returned to [AuthViewState]
 */
class AttemptRegistration(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val authService: AuthService,
    private val emailDataStore: EmailDataStore
) {

    fun execute(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>?> = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            authService.register(
                email,
                username,
                password,
                confirmPassword
            )
        }
        emit(
            object : ApiResponseHandler<AuthViewState, RegistrationResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: RegistrationResponse): DataState<AuthViewState> {

                    if (resultObj.response == GENERIC_AUTH_ERROR) {
                        return DataState.error(
                            response = Response(
                                resultObj.errorMessage,
                                UiType.Dialog,
                                MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    }


                    val result1 = accountPropertiesDao.insertAndReplace(
                        AccountProperties(
                            resultObj.pk,
                            resultObj.email,
                            resultObj.username
                        )
                    )
                    if (result1 < 0) {
                        return DataState.error(
                            response = Response(
                                ERROR_SAVE_ACCOUNT_PROPERTIES,
                                UiType.Dialog,
                                MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    }


                    val authToken = AuthToken(
                        resultObj.pk,
                        resultObj.token
                    )
                    val result2 = authTokenDao.insert(authToken)
                    if (result2 < 0) {
                        return DataState.error(
                            response = Response(
                                ERROR_SAVE_AUTH_TOKEN,
                                UiType.Dialog,
                                MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    }


                    emailDataStore.updateAuthenticatedUserEmail(email)


                    return DataState.data(
                        data = AuthViewState(authToken = authToken),
                        stateEvent = stateEvent,
                        response = null
                    )
                }
            }.getResult()
        )
    }

    companion object{
        const val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties.\nTry restarting the app."
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."
        const val GENERIC_AUTH_ERROR = "Error"
    }
}