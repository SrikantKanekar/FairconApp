package com.example.faircon.framework.presentation.ui.main.home

import com.example.faircon.business.domain.state.StateEvent
import com.example.faircon.framework.presentation.ui.BaseViewModel
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState

class HomeViewModel: BaseViewModel<HomeViewState>() {
    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleNewData(data: HomeViewState) {
        TODO("Not yet implemented")
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        TODO("Not yet implemented")
    }

}