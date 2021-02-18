package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.MainService
import com.example.faircon.framework.presentation.ui.main.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class ChangePassword(
    private val mainService: MainService
) {
    fun execute(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ) = flow {
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
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Success
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

}