package com.example.faircon.business.interactors.auth

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.auth.AuthTokenDao
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.datasource.cache.main.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.network.auth.AuthService
import com.example.faircon.framework.datasource.network.auth.response.RegistrationResponse
import com.example.faircon.framework.datasource.preference.MyPreferences
import com.example.faircon.framework.presentation.auth.state.AuthViewState
import com.example.faircon.framework.presentation.auth.state.RegistrationFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Registration(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val authService: AuthService,
    private val myPreferences: MyPreferences
) {

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>?> = flow {
        val registrationFieldErrors =
            RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (registrationFieldErrors == RegistrationFields.RegistrationError.none()) {

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
                                    UIComponentType.Dialog,
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
                        // will return -1 if failure
                        if (result1 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_ACCOUNT_PROPERTIES,
                                    UIComponentType.Dialog,
                                    MessageType.Error
                                ),
                                stateEvent = stateEvent
                            )
                        }

                        // will return -1 if failure
                        val authToken = AuthToken(
                            resultObj.pk,
                            resultObj.token
                        )
                        val result2 = authTokenDao.insert(authToken)
                        if (result2 < 0) {
                            return DataState.error(
                                response = Response(
                                    ERROR_SAVE_AUTH_TOKEN,
                                    UIComponentType.Dialog,
                                    MessageType.Error
                                ),
                                stateEvent = stateEvent
                            )
                        }
                        myPreferences.saveAuthenticatedUser(email)
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
                        message = "${stateEvent.errorInfo()}\n\nReason: $registrationFieldErrors",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        }
    }

    companion object{
        const val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties.\nTry restarting the app."
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."
        const val GENERIC_AUTH_ERROR = "Error"
    }
}