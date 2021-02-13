package com.example.faircon.framework.presentation.ui.auth

import com.example.faircon.business.domain.state.DataState
import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.business.interactors.auth.AuthInteractors
import com.example.faircon.framework.datasource.cache.authToken.AuthToken
import com.example.faircon.framework.presentation.ui.BaseViewModel
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


    // Login Setters
    fun setLoginEmail(email: String) {
        val update = getCurrentViewStateOrNew()
        update.loginFields.login_email = email
        setViewState(update)
    }

    fun setLoginPassword(password: String) {
        val update = getCurrentViewStateOrNew()
        update.loginFields.login_password = password
        setViewState(update)
    }


    // Registration Setters
    fun setRegistrationEmail(email: String) {
        val update = getCurrentViewStateOrNew()
        update.registrationFields.registration_email = email
        setViewState(update)
    }

    fun setRegistrationUsername(username: String) {
        val update = getCurrentViewStateOrNew()
        update.registrationFields.registration_username = username
        setViewState(update)
    }

    fun setRegistrationPassword(password: String) {
        val update = getCurrentViewStateOrNew()
        update.registrationFields.registration_password = password
        setViewState(update)
    }

    fun setRegistrationConfirmPassword(password: String) {
        val update = getCurrentViewStateOrNew()
        update.registrationFields.registration_confirm_password = password
        setViewState(update)
    }


    // AuthToken Setter
    private fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
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