package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.datasource.cache.main.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.MainService
import com.example.faircon.framework.presentation.main.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class SaveAccountProperties(
    private val mainService: MainService,
    private val accountPropertiesDao: AccountPropertiesDao
) {
    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ) = flow {

        val updateFieldErrors = AccountProperties(1, email, username).isValidForUpdating()
        if (updateFieldErrors == AccountProperties.UpdateAccountError.none()) {
            val apiResult = safeApiCall(Dispatchers.IO) {
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
                DataState.error<AccountViewState>(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\n\nReason: $updateFieldErrors",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        }
    }
}