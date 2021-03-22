package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.spotifyclone.business.domain.state.ViewState

data class HomeViewState(
    var connected: Boolean? = null
) : ViewState