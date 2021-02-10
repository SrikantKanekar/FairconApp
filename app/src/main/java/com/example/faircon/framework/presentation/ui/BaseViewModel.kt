package com.example.faircon.framework.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faircon.business.domain.state.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel<ViewState> : ViewModel() {

    private val _viewState: MutableLiveData<ViewState> by lazy { MutableLiveData(initNewViewState()) }

    val viewState: LiveData<ViewState>
        get() = _viewState

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    abstract fun initNewViewState(): ViewState

    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value ?: initNewViewState()
    }

    private val dataChannelManager: DataChannelManager<ViewState> =
        object : DataChannelManager<ViewState>() {
            override fun handleNewData(data: ViewState) {
                this@BaseViewModel.handleNewData(data)
            }
        }

    val shouldDisplayProgressBar = dataChannelManager.shouldDisplayProgressBar

    abstract fun handleNewData(data: ViewState)

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>?>
    ) {
        dataChannelManager.launchJob(stateEvent, jobFunction)
    }

    fun emitStateMessageEvent(
        stateMessage: StateMessage,
        stateEvent: StateEvent
    ) = flow {
        emit(
            DataState.error<ViewState>(
                response = stateMessage.response,
                stateEvent = stateEvent
            )
        )
    }

    fun emitInvalidStateEvent(
        stateEvent: StateEvent
    ) = flow {
        emit(
            DataState.error<ViewState>(
                response = Response(
                    message = INVALID_STATE_EVENT,
                    uiComponentType = UIComponentType.None,
                    messageType = MessageType.Error
                ),
                stateEvent = stateEvent
            )
        )
    }


    // StateEventManager Functions
    abstract fun setStateEvent(stateEvent: StateEvent)

    fun clearActiveStateEvents() = dataChannelManager.clearActiveStateEvents()


    // MessageStack Functions
    val stateMessage = dataChannelManager.messageStack.stateMessage

    fun removeStateMessage(index: Int = 0) = dataChannelManager.removeStateMessage(index)

    fun clearAllStateMessages() = dataChannelManager.clearAllStateMessages()

    fun printAllStateMessages() = dataChannelManager.printAllStateMessages()

    fun getTotalStateMessages() = dataChannelManager.messageStack.size


    // Channel Scope Functions
    fun cancelActiveJobs() = dataChannelManager.cancelActiveJobs()


    companion object{
        const val INVALID_STATE_EVENT = "Invalid state event"
    }
}