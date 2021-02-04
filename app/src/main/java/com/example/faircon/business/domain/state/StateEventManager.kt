package com.example.faircon.business.domain.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faircon.business.domain.util.printLogD

class StateEventManager {

    private val activeStateEvents: HashMap<String, StateEvent> = HashMap()
//    val shouldDisplayProgressBar = mutableStateOf(false)

    private val _shouldDisplayProgressbar = MutableLiveData<Boolean>()

    val shouldDisplayProgressBar : LiveData<Boolean>
        get() = _shouldDisplayProgressbar

    fun isStateEventActive(stateEvent: StateEvent): Boolean {
        printLogD(
            "StateEventManager", "Is StateEvent Active? : " +
                    "${activeStateEvents.containsKey(stateEvent.eventName())}"
        )
        return activeStateEvents.containsKey(stateEvent.eventName())
    }

    fun addStateEvent(stateEvent: StateEvent) {
        printLogD(
            "StateEventManager",
            "Launching New StateEvent --------> ${stateEvent.eventName()}"
        )
        activeStateEvents[stateEvent.eventName()] = stateEvent
        syncProgressbar()
    }

    fun removeStateEvent(stateEvent: StateEvent?) {
        printLogD("StateEventManager", "Removed StateEvent ----> ${stateEvent?.eventName()}")
        activeStateEvents.remove(stateEvent?.eventName())
        syncProgressbar()
    }

    fun getActiveStateEvents(): MutableSet<String> {
        return activeStateEvents.keys
    }

    fun clearActiveStateEvents() {
        printLogD("StateEventManager", "Clearing active state events")
        activeStateEvents.clear()
        syncProgressbar()
    }

    private fun syncProgressbar() {
        var progressBar = false
        for (stateEvent in activeStateEvents.values) {
            if (stateEvent.shouldDisplayProgressBar()) {
                progressBar = true
            }
        }
        _shouldDisplayProgressbar.value = progressBar
        printLogD("StateEventManager", "ProgressBar $progressBar ")
    }
}