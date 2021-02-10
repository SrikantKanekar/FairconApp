package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.MainService
import com.example.faircon.framework.presentation.ui.main.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class UpdatePassword(
    private val mainService: MainService
) {
    fun updatePassword(
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
            val apiResult = safeApiCall(Dispatchers.IO) {
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
                DataState.error<AccountViewState>(
                    response = Response(
                        message = "${stateEvent.errorInfo()}\n\nReason: $changePasswordErrors",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            )
        }
    }
}