package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.MainService
import com.example.faircon.framework.presentation.ui.main.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class UpdateAccountProperties(
    private val mainService: MainService,
    private val accountPropertiesDao: AccountPropertiesDao
) {
    fun update(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ) = flow {

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
                        data = AccountViewState(
                            accountProperties = updatedAccountProperties
                        ),
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