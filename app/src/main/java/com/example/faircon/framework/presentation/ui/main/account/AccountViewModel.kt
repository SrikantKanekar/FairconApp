package com.example.faircon.framework.presentation.ui.main.account

import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.main.account.AccountInteractors
import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties
import com.example.faircon.framework.datasource.session.SessionManager
import com.example.faircon.framework.presentation.components.snackbar.SnackbarController
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent.*
import com.example.faircon.framework.presentation.ui.main.account.state.AccountViewState
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

    val snackbarController = SnackbarController(viewModelScope)

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<AccountViewState>?> = when (stateEvent) {

                is GetAccountPropertiesEvent -> {
                    accountInteractors.getAccountProperties.getAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }

                is UpdateAccountPropertiesEvent -> {
                    accountInteractors.updateAccountProperties.update(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is ChangePasswordEvent -> {
                    accountInteractors.changePassword.execute(
                        stateEvent = stateEvent,
                        authToken = authToken,
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
            AccountStateEvent.UpdateAccountPropertiesEvent(
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
            AccountStateEvent.ChangePasswordEvent(
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