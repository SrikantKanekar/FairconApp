package com.example.faircon.ui

import com.example.faircon.util.Response
import com.example.faircon.util.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)
}