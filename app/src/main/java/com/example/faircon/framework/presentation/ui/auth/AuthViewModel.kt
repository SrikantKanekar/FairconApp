package com.example.faircon.framework.presentation.ui.auth

import com.example.faircon.business.domain.state.*
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.auth.passwordReset.WebAppInterface
import com.example.faircon.framework.presentation.ui.auth.state.AuthStateEvent.*
import com.example.faircon.framework.presentation.ui.auth.state.AuthViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val authInteractors: AuthInteractors
) : BaseViewModel<AuthViewState>() {

    init {
        setStateEvent(CheckPreviousAuthEvent)
    }

    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setViewState(viewState.value.copy(authToken = authToken))
        }
        data.previousUserCheck?.let { checked ->
            setViewState(viewState.value.copy(previousUserCheck = checked))
        }
        data.resetPasswordSuccess?.let { success ->
            setViewState(viewState.value.copy(resetPasswordSuccess = success))
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<AuthViewState>?> = when (stateEvent) {

            is LoginAttemptEvent -> {
                authInteractors.attemptLogin.execute(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                authInteractors.attemptRegistration.execute(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is CheckPreviousAuthEvent -> {
                authInteractors.checkPreviousUser.execute(stateEvent)
            }

            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    override fun initViewState(): AuthViewState {
        return AuthViewState()
    }

    fun login(
        email: String,
        password: String
    ) {
        setStateEvent(LoginAttemptEvent(email, password))
    }

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String,
    ) {
        setStateEvent(
            RegisterAttemptEvent(
                email,
                username,
                password,
                confirmPassword
            )
        )
    }

    val webInteractionCallback = object : WebAppInterface.OnWebInteractionCallback {

        override fun onError(errorMessage: String) {
            printLogD("AuthViewModel", "onError: $errorMessage")

            addNewStateMessage(
                StateMessage(
                    response = Response(
                        message = errorMessage,
                        uiType = UiType.Dialog,
                        messageType = MessageType.Error
                    )
                )
            )
        }

        override fun onSuccess(email: String) {
            printLogD("AuthViewModel", "onSuccess: a reset link will be sent to $email.")
            setViewState(viewState.value.copy(resetPasswordSuccess = true))
        }

        override fun onLoading(isLoading: Boolean) {
            printLogD("AuthViewModel", "onLoading... ")
            shouldDisplayProgressBar.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}