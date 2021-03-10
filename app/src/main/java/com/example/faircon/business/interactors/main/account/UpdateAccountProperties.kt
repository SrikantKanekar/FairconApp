package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.safeApiCall
import com.example.faircon.business.data.network.ApiResponseHandler
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.network.GenericResponse
import com.example.faircon.framework.datasource.network.main.AccountService
import com.example.faircon.framework.presentation.ui.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

/**
 * Called to Update user Email and Username
 * - Network request to Update
 * - Network request to get updated values
 * - AccountProperties database is updated
 * - updated values are returned to user
 */
class UpdateAccountProperties(
    private val accountService: AccountService,
    private val accountPropertiesDao: AccountPropertiesDao
) {
    fun execute(
        email: String,
        username: String,
        stateEvent: StateEvent
    ) = flow {

        val apiResult = safeApiCall(Dispatchers.IO) {
            accountService.updateAccountProperties(
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

                    val updatedAccountProperties = accountService.getAccountProperties()

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