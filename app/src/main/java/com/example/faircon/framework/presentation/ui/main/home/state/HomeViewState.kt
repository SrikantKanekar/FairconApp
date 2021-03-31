package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.faircon.business.domain.model.Faircon
import com.example.spotifyclone.business.domain.state.ViewState

data class HomeViewState(
    var faircon: Faircon? = null,
    var serverConnected: Boolean? = null,
    var webSocketConnected: Boolean? = null
) : ViewState