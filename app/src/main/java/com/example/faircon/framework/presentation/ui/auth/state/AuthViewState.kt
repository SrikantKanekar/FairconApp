package com.example.faircon.framework.presentation.ui.auth.state

import com.example.faircon.framework.datasource.cache.entity.AuthToken

data class AuthViewState(
    var authToken: AuthToken? = null,
    var previousUserCheck: Boolean? = null,
    var resetPasswordSuccess: Boolean? = null
)