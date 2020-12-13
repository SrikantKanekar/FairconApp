package com.example.faircon.repository.main

import com.example.faircon.api.GenericResponse
import com.example.faircon.api.main.MainService
import com.example.faircon.models.AccountProperties
import com.example.faircon.models.AuthToken
import com.example.faircon.persistence.AccountPropertiesDao
import com.example.faircon.repository.NetworkBoundResource
import com.example.faircon.repository.buildError
import com.example.faircon.repository.safeApiCall
import com.example.faircon.session.SessionManager
import com.example.faircon.ui.auth.state.AuthViewState
import com.example.faircon.ui.auth.state.LoginFields
import com.example.faircon.ui.main.account.state.AccountViewState
import com.example.faircon.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl
@Inject
constructor(
    val mainService: MainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
) : AccountRepository {
    override fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    mainService.getAccountProperties("Token ${authToken.token!!}")
                },
                cacheCall = {
                    accountPropertiesDao.searchByPk(authToken.account_pk!!)
                }
            ) {
            override suspend fun updateCache(networkObject: AccountProperties) {
                accountPropertiesDao.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username
                )
            }

            override fun handleCacheSuccess(
                resultObj: AccountProperties
            ): DataState<AccountViewState> {
                return DataState.data(
                    response = null,
                    data = AccountViewState(
                        accountProperties = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.result
    }

    override fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ) = flow {

        val updateFieldErrors = AccountProperties(1, email, username).isValidForUpdating()
        if (updateFieldErrors == AccountProperties.UpdateAccountError.none()) {
            val apiResult = safeApiCall(IO) {
                mainService.saveAccountProperties(
                    authorization = "Token ${authToken.token!!}",
                    email = email,
                    username = username
                )
            }
            emit(
                object : ApiResponseHandler<AccountViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(resultObj: GenericResponse): DataState<AccountViewState> {

                        val updatedAccountProperties = mainService
                            .getAccountProperties("Token ${authToken.token!!}")

                        accountPropertiesDao.updateAccountProperties(
                            pk = updatedAccountProperties.pk,
                            email = updatedAccountProperties.email,
                            username = updatedAccountProperties.username
                        )

                        return DataState.data(
                            data = null,
                            response = Response(
                                message = resultObj.response,
                                uiComponentType = UIComponentType.Toast,
                                messageType = MessageType.Success
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AccountViewState>(
                    updateFieldErrors,
                    UIComponentType.Dialog,
                    stateEvent
                )
            )
        }
    }

    override fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ) = flow {
        val changePasswordErrors = AccountProperties
            .isValidForChangingPassword(
                currentPassword,
                newPassword,
                confirmNewPassword
            )
        if (changePasswordErrors == AccountProperties.ChangePasswordError.none()) {
            val apiResult = safeApiCall(IO) {
                mainService.updatePassword(
                    authorization = "Token ${authToken.token!!}",
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            }
            emit(
                object : ApiResponseHandler<AccountViewState, GenericResponse>(
                    response = apiResult,
                    stateEvent = stateEvent
                ) {
                    override suspend fun handleSuccess(
                        resultObj: GenericResponse
                    ): DataState<AccountViewState> {

                        return DataState.data(
                            data = null,
                            response = Response(
                                message = resultObj.response,
                                uiComponentType = UIComponentType.Toast,
                                messageType = MessageType.Success
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.getResult()
            )
        } else {
            emit(
                buildError<AccountViewState>(
                    changePasswordErrors,
                    UIComponentType.Dialog,
                    stateEvent
                )
            )
        }
    }
}