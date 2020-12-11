package com.example.faircon.util

import com.example.faircon.ui.AreYouSureCallback

data class Response(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

data class StateMessage(val response: Response)

sealed class UIComponentType {
    object Toast : UIComponentType()
    object Dialog : UIComponentType()
    class AreYouSureDialog(val callback: AreYouSureCallback) : UIComponentType()
    object None : UIComponentType()
}

sealed class MessageType {
    object Success : MessageType()
    object Error : MessageType()
    object Info : MessageType()
    object None : MessageType()
}

interface StateMessageCallback {
    fun removeMessageFromStack()
}