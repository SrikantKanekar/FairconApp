package com.example.faircon.framework.presentation.ui.auth.state

import com.example.faircon.framework.datasource.cache.authToken.AuthToken

data class AuthViewState(
    var authToken: AuthToken? = null
)