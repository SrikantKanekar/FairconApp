package com.example.faircon.framework.presentation.ui

import com.example.faircon.business.domain.state.Response
import com.example.faircon.business.domain.state.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)
}