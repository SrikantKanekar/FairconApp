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
import androidx.navigation.fragment.findNavController
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.framework.presentation.components.LoadingButton
import com.example.faircon.framework.presentation.components.MyPasswordTextField
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
                    displayProgressBar = false
                ) {

                    val changePasswordFields = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .changePasswordFields

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

                            MyPasswordTextField(
                                onValueChange = { changePasswordFields.currentPassword = it },
                                label = "Current password",
                                onImeAction = {
                                    newPasswordFocusRequester.requestFocus()
                                }
                            )

                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(newPasswordFocusRequester),
                                onValueChange = { changePasswordFields.newPassword = it },
                                label = "New password",
                                onImeAction = {
                                    confirmPasswordFocusRequester.requestFocus()
                                }
                            )

                            MyPasswordTextField(
                                modifier = Modifier.focusRequester(confirmPasswordFocusRequester),
                                onValueChange = { changePasswordFields.confirmPassword = it },
                                label = "Confirm new password",
                                imeAction = ImeAction.Done,
                                onImeAction = {
                                    viewModel.setStateEvent(
                                        AccountStateEvent.ChangePasswordEvent(
                                            changePasswordFields.currentPassword,
                                            changePasswordFields.newPassword,
                                            changePasswordFields.confirmPassword
                                        )
                                    )
                                }
                            )

                            LoadingButton(
                                isLoading = viewModel.shouldDisplayProgressBar.value,
                                text = "Update password",
                                onClick = {
                                    viewModel.setStateEvent(
                                        AccountStateEvent.ChangePasswordEvent(
                                            changePasswordFields.currentPassword,
                                            changePasswordFields.newPassword,
                                            changePasswordFields.confirmPassword
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