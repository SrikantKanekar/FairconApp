package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.model.Parameter
import com.example.spotifyclone.business.domain.state.ViewState

data class HomeViewState(
    var parameter: Parameter? = null,
    var syncedController: Boolean? = null,
    var serverConnected: Boolean? = null,
    var webSocketConnected: Boolean? = null
) : ViewState