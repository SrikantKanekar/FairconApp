package com.example.faircon.business.interactors.main.account

import com.example.faircon.business.data.common.NetworkBoundResource
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
import com.example.faircon.framework.datasource.cache.accountProperties.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties
import com.example.faircon.framework.datasource.network.main.AccountService
import com.example.faircon.framework.presentation.ui.account.state.AccountViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Get the user Account Information. Called when user visits the Account Screen.
 * - Cache call is made to [AccountProperties] database, and data is shown to user
 * - Network request is made in background for the same
 * - Once the requests is successful, new data is stored in [AccountProperties] database and shown to user
 */
class GetAccountProperties(
    private val accountService: AccountService,
    private val accountPropertiesDao: AccountPropertiesDao
) {

    fun execute(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>?> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                dispatcher = Dispatchers.IO,
                stateEvent = stateEvent,
                apiCall = {
                    accountService.getAccountProperties()
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
}