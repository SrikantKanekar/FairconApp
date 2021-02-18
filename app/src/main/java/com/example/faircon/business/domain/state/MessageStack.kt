package com.example.faircon.business.domain.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.faircon.business.domain.util.printLogD

class MessageStack: ArrayList<StateMessage>() {

    val stateMessage: MutableState<StateMessage?> = mutableStateOf(null)

    private fun setStateMessage(message: StateMessage?){
        stateMessage.value = message
    }

//    private val _stateMessage: MutableLiveData<StateMessage?> = MutableLiveData()
//
//    val stateMessage: LiveData<StateMessage?>
//        get() = _stateMessage


    override fun add(element: StateMessage): Boolean {
        if (this.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        val transaction = super.add(element)
        printLogD(
            "MessageStack",
            "Adding New StateMessage \n" +
                    "Message : ${element.response.message} \n" +
                    "UiComponentType : ${element.response.uiType} \n" +
                    "MessageType : ${element.response.messageType}"
        )
        if (this.size == 1) {
            setStateMessage(message = element)
        }
        return transaction
    }

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for (element in elements) {
            add(element)
        }
        return true // always return true. We don't care about result bool.
    }

    override fun removeAt(index: Int): StateMessage {
        try {
            val transaction = super.removeAt(index)
            printLogD("MessageStack", "Removed State message at index $index")
            if (this.size > 0) {
                setStateMessage(message = this[0])
            } else {
                printLogD("MessageStack", "Message stack is empty")
                setStateMessage(null)
            }
            return transaction
        } catch (e: IndexOutOfBoundsException) {
            setStateMessage(null)
            e.printStackTrace()
        }
        return StateMessage( // this does nothing
            Response(
                message = "does nothing",
                uiType = UiType.None,
                messageType = MessageType.Info
            )
        )
    }

    fun isStackEmpty(): Boolean {
        printLogD("MessageStack", "Is StateMessage Empty? : ${size == 0}")
        return size == 0
    }
}