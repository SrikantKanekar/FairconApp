package com.example.faircon.repository.main

import com.example.faircon.models.AuthToken
import com.example.faircon.ui.main.account.state.AccountViewState
import com.example.faircon.util.DataState
import com.example.faircon.util.StateEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>
}