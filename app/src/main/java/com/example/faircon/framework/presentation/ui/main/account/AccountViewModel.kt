package com.example.faircon.framework.presentation.ui.main.account

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.main.account.AccountInteractors
import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties
import com.example.faircon.framework.datasource.session.SessionManager
import com.example.faircon.framework.presentation.components.GenericDialogInfo
import com.example.faircon.framework.presentation.components.PositiveAction
import com.example.faircon.framework.presentation.components.snackbar.SnackbarController
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent.*
import com.example.faircon.framework.presentation.ui.main.account.state.AccountViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*
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

    // Queue for "First-In-First-Out" behavior
    val messageQueue: MutableState<Queue<GenericDialogInfo>> = mutableStateOf(LinkedList())

    fun removeHeadMessage() {
        if (messageQueue.value.isNotEmpty()) {
            val update = messageQueue.value
            update.remove() // remove first (oldest message)
            messageQueue.value = LinkedList() // force recompose (bug?)
            messageQueue.value = update
        }
    }

    fun appendErrorMessage(title: String, description: String) {
        messageQueue.value.offer(
            GenericDialogInfo.Builder(
                title = title,
                onDismiss = { removeHeadMessage() }
            )
                .description(description)
                .positive(
                    PositiveAction(
                        positiveBtnTxt = "Ok",
                        onPositiveAction = { removeHeadMessage() },
                    )
                )
                .build()
        )
    }


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
                    accountInteractors.saveAccountProperties.saveAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is ChangePasswordEvent -> {
                    accountInteractors.updatePassword.updatePassword(
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

    fun logout() {
        sessionManager.logout()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}