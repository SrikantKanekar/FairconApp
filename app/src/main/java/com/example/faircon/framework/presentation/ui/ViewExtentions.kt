package com.example.faircon.framework.presentation.ui

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.faircon.business.domain.state.StateMessageCallback

fun Activity.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}



