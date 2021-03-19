package com.example.faircon.framework.presentation.ui.account.state

import com.example.faircon.framework.datasource.cache.accountProperties.AccountProperties

data class AccountViewState(
    var accountProperties: AccountProperties? = null
)