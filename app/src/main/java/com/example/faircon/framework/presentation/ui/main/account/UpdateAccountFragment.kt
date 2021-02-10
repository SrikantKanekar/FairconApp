package com.example.faircon.framework.presentation.ui.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.MyEmailTextField
import com.example.faircon.framework.presentation.components.MyOutlinedTextField
import com.example.faircon.framework.presentation.components.MyUsernameTextField
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent

class UpdateAccountFragment : BaseAccountFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savedInstanceState?.let { inState ->
//            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
//                viewModel.setViewState(viewState)
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FairconTheme(
                    displayProgressBar = false
                ) {

                    val accountProperties = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .accountProperties

                    Scaffold {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(170.dp))

                            val updateAccountField = viewModel
                                .viewState
                                .observeAsState(viewModel.getCurrentViewStateOrNew())
                                .value
                                .updateAccountFields

                            accountProperties?.let { accountProperties ->

                                updateAccountField.email = accountProperties.email
                                updateAccountField.username = accountProperties.username
                                val focusRequester = remember { FocusRequester() }

                                MyEmailTextField(
                                    initialValue = updateAccountField.email,
                                    onValueChange = { updateAccountField.email = it },
                                    onImeAction = {
                                        focusRequester.requestFocus()
                                    }
                                )

                                MyUsernameTextField(
                                    modifier = Modifier.focusRequester(focusRequester),
                                    initialValue = updateAccountField.username,
                                    onValueChange = { updateAccountField.username = it },
                                    imeAction = ImeAction.Done,
                                    onImeAction = {
                                        viewModel.setStateEvent(
                                            AccountStateEvent.UpdateAccountPropertiesEvent(
                                                updateAccountField.email,
                                                updateAccountField.username
                                            )
                                        )
                                    }
                                )

                                LoadingButton(
                                    isLoading = viewModel.shouldDisplayProgressBar.value,
                                    text = "Update",
                                    onClick = {
                                        viewModel.setStateEvent(
                                            AccountStateEvent.UpdateAccountPropertiesEvent(
                                                updateAccountField.email,
                                                updateAccountField.username
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.removeStateMessage()
                        }
                    }
                )
            }
        })
    }
}