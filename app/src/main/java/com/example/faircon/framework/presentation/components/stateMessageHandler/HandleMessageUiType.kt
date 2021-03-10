package com.example.faircon.framework.presentation.components.stateMessageHandler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.faircon.business.domain.state.StateMessage
import com.example.faircon.business.domain.state.UiType
import com.example.faircon.business.domain.util.printLogD


@Composable
fun HandleMessageUiType(
    stateMessage: StateMessage?,
    scaffoldState: ScaffoldState,
    removeStateMessage: () -> Unit = {}
) {
    printLogD("HandleMessageUiType", stateMessage.toString())

    if (stateMessage?.response?.message != null) {
        stateMessage.response.let { response ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                when (response.uiType) {

                    UiType.Dialog -> {
                        DialogMessageType(
                            response = response,
                            removeStateMessage = removeStateMessage
                        )
                    }

                    UiType.SnackBar -> {
                        SnackbarMessageType(
                            response = response,
                            scaffoldState = scaffoldState,
                            removeStateMessage = removeStateMessage
                        )
                    }

                    UiType.AreYouSureDialog -> {
                        removeStateMessage()
                    }

                    UiType.None -> {
                        removeStateMessage()
                    }
                }
            }
        }
    } else {
        removeStateMessage()
    }
}
