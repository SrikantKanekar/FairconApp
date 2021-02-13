package com.example.faircon.framework.presentation.ui.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.faircon.framework.presentation.components.textField.EmailState
import com.example.faircon.framework.presentation.components.textField.MyEmailTextField
import com.example.faircon.framework.presentation.components.textField.MyUsernameTextField
import com.example.faircon.framework.presentation.components.textField.UsernameState
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
                    darkTheme = true,
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

                            accountProperties?.let { accountProperties ->

                                val focusRequester = remember { FocusRequester() }

                                val emailState = remember { EmailState(accountProperties.email) }
                                MyEmailTextField(
                                    emailState = emailState,
                                    onImeAction = { focusRequester.requestFocus() }
                                )

                                val usernameState =
                                    remember { UsernameState(accountProperties.username) }
                                MyUsernameTextField(
                                    modifier = Modifier.padding(top = 10.dp)
                                        .focusRequester(focusRequester),
                                    usernameState = usernameState,
                                    imeAction = ImeAction.Done,
                                    onImeAction = {
                                        if (emailState.isValid && usernameState.isValid) {
                                            updateAccount(
                                                emailState.text,
                                                usernameState.text
                                            )
                                        }
                                    }
                                )

                                LoadingButton(
                                    isLoading = viewModel.shouldDisplayProgressBar.value,
                                    text = "Update",
                                    enabled = emailState.isValid
                                            && usernameState.isValid,
                                    onClick = {
                                        updateAccount(
                                            emailState.text,
                                            usernameState.text
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

    private fun updateAccount(
        email: String,
        username: String
    ) {
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                email,
                username
            )
        )
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