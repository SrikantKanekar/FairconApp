package com.example.faircon.framework.presentation.ui.auth

import androidx.compose.runtime.mutableStateOf
import com.example.faircon.business.domain.state.*
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
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

    val checkPreviousUser = mutableStateOf(false)
    val resetPasswordSuccess = mutableStateOf(false)

    init {
        setStateEvent(CheckPreviousAuthEvent)
    }

    override fun handleNewData(data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<AuthViewState>?> = when (stateEvent) {

            is LoginAttemptEvent -> {
                authInteractors.login.attemptLogin(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                authInteractors.registration.attemptRegistration(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is CheckPreviousAuthEvent -> {
                authInteractors.checkPreviousUser.checkPreviousAuthUser(stateEvent)
            }

            else -> emitInvalidStateEvent(stateEvent)
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun login(
        email: String,
        password: String
    ) {
        setStateEvent(
            LoginAttemptEvent(email, password)
        )
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

    // AuthToken Setter
    private fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken != authToken) {
            update.authToken = authToken
            setViewState(update)
        }
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
            resetPasswordSuccess.value = true
        }

        override fun onLoading(isLoading: Boolean) {
            printLogD("AuthViewModel", "onLoading... ")
            //Removed because the website has its progress bar
            shouldDisplayProgressBar.value = true
        }
    }


    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}