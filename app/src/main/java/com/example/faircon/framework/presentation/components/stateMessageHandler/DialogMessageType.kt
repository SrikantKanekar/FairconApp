package com.example.faircon.framework.presentation.components.stateMessageHandler

import androidx.compose.runtime.Composable
import com.example.faircon.business.domain.state.MessageType
import com.example.faircon.business.domain.state.Response
import com.example.faircon.framework.presentation.components.dialog.MyErrorDialog
import com.example.faircon.framework.presentation.components.dialog.MyInfoDialog
import com.example.faircon.framework.presentation.components.dialog.MySuccessDialog

@Composable
fun DialogMessageType(
    response: Response,
    removeStateMessage: () -> Unit
) {
    response.message?.let { message ->
        when(response.messageType){

            MessageType.Success -> {
                MySuccessDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }

            MessageType.Error -> {
                MyErrorDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }

            MessageType.Info -> {
                MyInfoDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }
        }
    }
}