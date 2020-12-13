package com.example.faircon.ui.main.account

import androidx.hilt.lifecycle.ViewModelInject
import com.example.faircon.models.AccountProperties
import com.example.faircon.repository.main.AccountRepository
import com.example.faircon.session.SessionManager
import com.example.faircon.ui.BaseViewModel
import com.example.faircon.ui.main.account.state.AccountStateEvent.*
import com.example.faircon.ui.main.account.state.AccountViewState
import com.example.faircon.util.*
import com.example.faircon.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountViewModel
@ViewModelInject
constructor(
    private val sessionManager: SessionManager,
    private val accountRepository: AccountRepository
) : BaseViewModel<AccountViewState>() {

    override fun handleNewData(data: AccountViewState) {
        data.accountProperties?.let { accountProperties ->
            setAccountPropertiesData(accountProperties)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<AccountViewState>> = when (stateEvent) {

                is GetAccountPropertiesEvent -> {
                    accountRepository.getAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }

                is UpdateAccountPropertiesEvent -> {
                    accountRepository.saveAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is ChangePasswordEvent -> {
                    accountRepository.updatePassword(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        currentPassword = stateEvent.currentPassword,
                        newPassword = stateEvent.newPassword,
                        confirmNewPassword = stateEvent.confirmNewPassword
                    )
                }

                else -> {
                    flow {
                        emit(
                            DataState.error<AccountViewState>(
                                response = Response(
                                    message = INVALID_STATE_EVENT,
                                    uiComponentType = UIComponentType.None,
                                    messageType = MessageType.Error
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
            launchJob(stateEvent, job)
        }
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun logout() {
        sessionManager.logout()
    }

    private fun setAccountPropertiesData(accountProperties: AccountProperties) {
        val update = getCurrentViewStateOrNew()
        if (update.accountProperties == accountProperties) {
            return
        }
        update.accountProperties = accountProperties
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}