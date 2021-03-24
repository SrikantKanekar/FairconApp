package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.entity.AccountProperties
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.entity.AuthToken
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.dataStore.EmailDataStore
import com.example.faircon.framework.datasource.network.services.AuthService
import com.example.faircon.framework.datasource.network.response.LoginResponse
import com.example.faircon.framework.presentation.ui.auth.state.AuthViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
/**
 * Attempts to login the user
 * - make login attempt
 * - If the login credentials are Invalid, [GENERIC_AUTH_ERROR] string is returned from the server and [INVALID_CREDENTIALS] is returned
 * - If the login credentials are correct, pk and email is inserted into [AccountProperties] database
 * - Similarly, pk and token is inserted into [AuthToken] database
 * - If -1 is returned, [ERROR_SAVE_AUTH_TOKEN] is shown to user
 * - email is stored in [EmailDataStore]
 * - [AuthToken] is returned to [AuthViewState]
 */
class AttemptLogin(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val authService: AuthService,
    private val emailDataStore: EmailDataStore
) {

    fun execute(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>?> = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            authService.login(email, password)
        }

        emit(
            object : ApiResponseHandler<AuthViewState, LoginResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: LoginResponse): DataState<AuthViewState> {

                    // Incorrect login credentials counts as a 200 response from server, so need to handle that
                    if (resultObj.response == GENERIC_AUTH_ERROR) {
                        return DataState.error(
                            response = Response(
                                INVALID_CREDENTIALS,
                                UiType.SnackBar,
                                MessageType.Error
                            ),
                            stateEvent = stateEvent
                        )
                    }
                    accountPropertiesDao.insertOrIgnore(
                        AccountProperties(
                            resultObj.pk,
                            resultObj.email,
                            ""
                        )
                    )


                    val authToken = AuthToken(
                        resultObj.pk,
                        resultObj.token
                    )
                    val result = authTokenDao.insert(authToken)
                    if (result < 0) {
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
                        data = AuthViewState(
                            authToken = authToken
                        ),
                        response = Response(
                            message = "Logged in User",
                            uiType = UiType.None,
                            messageType = MessageType.Success
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

    companion object {
        const val ERROR_SAVE_AUTH_TOKEN =
            "Error saving authentication token.\nTry restarting the app."
        const val GENERIC_AUTH_ERROR = "Error"
        const val INVALID_CREDENTIALS = "Invalid credentials"
    }
}