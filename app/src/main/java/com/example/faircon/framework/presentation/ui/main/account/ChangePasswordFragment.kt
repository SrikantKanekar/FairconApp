package com.example.faircon.framework.presentation.ui.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.textField.ConfirmPasswordState
import com.example.faircon.framework.presentation.components.textField.MyPasswordTextField
import com.example.faircon.framework.presentation.components.textField.PasswordState
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent

class ChangePasswordFragment : BaseAccountFragment() {

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

                    Scaffold {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(100.dp))

                            val newPasswordFocusRequester = remember { FocusRequester() }
                            val confirmPasswordFocusRequester = remember { FocusRequester() }


                            val currentPasswordState = remember { PasswordState() }
                            MyPasswordTextField(
                                passwordState = currentPasswordState,
                                label = "Current password",
                                onImeAction = { newPasswordFocusRequester.requestFocus() }
                            )

                            val newPasswordState = remember { PasswordState() }
                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(newPasswordFocusRequester),
                                passwordState = newPasswordState,
                                label = "New password",
                                onImeAction = { confirmPasswordFocusRequester.requestFocus() }
                            )

                            val confirmPasswordState =
                                remember { ConfirmPasswordState(newPasswordState) }
                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(confirmPasswordFocusRequester),
                                passwordState = confirmPasswordState,
                                label = "Confirm new password",
                                imeAction = ImeAction.Done,
                                onImeAction = {
                                    if (currentPasswordState.isValid && newPasswordState.isValid
                                        && confirmPasswordState.isValid
                                    ) {
                                        changePassword(
                                            currentPasswordState.text,
                                            newPasswordState.text,
                                            confirmPasswordState.text
                                        )
                                    }
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "Update password",
                                enabled = currentPasswordState.isValid
                                        && newPasswordState.isValid
                                        && confirmPasswordState.isValid,
                                onClick = {
                                    changePassword(
                                        currentPasswordState.text,
                                        newPasswordState.text,
                                        confirmPasswordState.text
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun changePassword(
        current: String,
        new: String,
        confirm: String
    ) {
        viewModel.setStateEvent(
            AccountStateEvent.ChangePasswordEvent(
                current,
                new,
                confirm
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
                if (stateMessage.response.message.equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)) {
                    findNavController().popBackStack()
                }
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

    companion object {
        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"
    }
}