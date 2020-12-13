package com.example.faircon.repository.auth

import com.example.faircon.ui.auth.state.AuthViewState
import com.example.faircon.util.DataState
import com.example.faircon.util.StateEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AuthRepository {

    fun attemptLogin(
        stateEvent: StateEvent,
        email: String,
        password: String
    ): Flow<DataState<AuthViewState>>

    fun attemptRegistration(
        stateEvent: StateEvent,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Flow<DataState<AuthViewState>>

    fun checkPreviousAuthUser(
        stateEvent: StateEvent
    ): Flow<DataState<AuthViewState>>

    fun returnNoTokenFound(
        stateEvent: StateEvent
    ): DataState<AuthViewState>
}
