package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.auth.AuthTokenDao
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.datasource.cache.main.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.network.auth.AuthService
import com.example.faircon.framework.datasource.network.auth.response.LoginResponse
import com.example.faircon.framework.datasource.preference.MyPreferences
import com.example.faircon.framework.presentation.ui.auth.state.AuthViewState
import com.example.faircon.framework.presentation.ui.auth.state.LoginFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Login(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val authService: AuthService,
    private val myPreferences: MyPreferences
) {

    fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>?> = flow {

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if (loginFieldErrors == LoginFields.LoginError.none()) {
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
                                    UIComponentType.Dialog,
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

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result = authTokenDao.insert(authToken)
                        if (result < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog,
                                    MessageType.Error
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        myPreferences.setAuthenticatedUser(email)

                        return DataState.data(
                            data = AuthViewState(
                                authToken = authToken
                            ),
                            stateEvent = stateEvent,
                            response = null
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                DataState.error<AuthViewState>(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\n\nReason: $loginFieldErrors",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        }
    }

    companion object{
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."
        const val GENERIC_AUTH_ERROR = "Error"
        const val INVALID_CREDENTIALS = "Invalid credentials"
    }
}