package com.example.faircon.framework.presentation.auth

import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.auth.state.AuthStateEvent.*
import com.example.faircon.framework.presentation.auth.state.AuthViewState
import com.example.faircon.framework.presentation.auth.state.LoginFields
import com.example.faircon.framework.presentation.auth.state.RegistrationFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val authInteractors: AuthInteractors
) : BaseViewModel<AuthViewState>() {

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

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = viewState.value!!
        if (update.registrationFields != registrationFields) {
            update.registrationFields = registrationFields
            setViewState(update)
        }
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = viewState.value!!
        if (update.loginFields != loginFields) {
            update.loginFields = loginFields
            setViewState(update)
        }
    }

    private fun setAuthToken(authToken: AuthToken) {
        val update = viewState.value!!
        if (update.authToken != authToken) {
            update.authToken = authToken
            setViewState(update)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}