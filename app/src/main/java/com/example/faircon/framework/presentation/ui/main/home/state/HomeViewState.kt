package com.example.faircon.framework.presentation.ui.main.home.state

import com.example.spotifyclone.business.domain.state.ViewState

data class HomeViewState(
    var syncedController: Boolean? = null,
    var connected: Boolean? = null,
    var clearedHomeDatastore: Boolean? = null
) : ViewState