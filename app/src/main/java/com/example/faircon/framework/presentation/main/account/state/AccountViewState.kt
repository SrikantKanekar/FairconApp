package com.example.faircon.framework.presentation.main.account.state

import android.os.Parcelable
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import kotlinx.android.parcel.Parcelize

const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "com.example.faircon.framework.presentation.AccountViewState"

@Parcelize
class AccountViewState(
    var accountProperties: AccountProperties? = null
) : Parcelable