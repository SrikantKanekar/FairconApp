package com.example.faircon.framework.presentation.ui.account

import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.main.account.AccountInteractors
import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties
import com.example.faircon.framework.datasource.session.SessionManager
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.account.state.AccountStateEvent
import com.example.faircon.framework.presentation.ui.account.state.AccountStateEvent.*
import com.example.faircon.framework.presentation.ui.account.state.AccountViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val accountInteractors: AccountInteractors,
) : BaseViewModel<AccountViewState>() {

    init {
        setStateEvent(GetAccountPropertiesEvent)
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<AccountViewState>?> = when (stateEvent) {

                is GetAccountPropertiesEvent -> {
                    accountInteractors.getAccountProperties.execute(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }

                is UpdateAccountPropertiesEvent -> {
                    accountInteractors.updateAccountProperties.execute(
                        stateEvent = stateEvent,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is ChangePasswordEvent -> {
                    accountInteractors.changePassword.execute(
                        stateEvent = stateEvent,
                        currentPassword = stateEvent.currentPassword,
                        newPassword = stateEvent.newPassword,
                        confirmNewPassword = stateEvent.confirmNewPassword
                    )
                }

                else -> emitInvalidStateEvent(stateEvent)
            }
            launchJob(stateEvent, job)
        }
    }

    override fun handleNewData(data: AccountViewState) {
        data.accountProperties?.let { accountProperties ->
            setAccountPropertiesData(accountProperties)
        }
    }

    private fun setAccountPropertiesData(accountProperties: AccountProperties) {
        val update = viewState.value!!
        if (update.accountProperties != accountProperties) {
            update.accountProperties = accountProperties
            setViewState(update)
        }
    }

    fun updateAccount(
        email: String,
        username: String
    ) {
        setStateEvent(
            UpdateAccountPropertiesEvent(
                email,
                username
            )
        )
    }

    fun changePassword(
        current: String,
        new: String,
        confirm: String
    ) {
        setStateEvent(
            ChangePasswordEvent(
                current,
                new,
                confirm
            )
        )
    }

    fun logout() {
        sessionManager.logout()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}