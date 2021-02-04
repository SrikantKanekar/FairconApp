package com.example.faircon.framework.presentation.main.account

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.framework.presentation.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.example.faircon.framework.presentation.main.account.state.AccountStateEvent
import com.example.faircon.framework.presentation.main.account.state.AccountViewState
import com.example.faircon.business.domain.state.StateMessageCallback
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment: BaseAccountFragment(R.layout.fragment_change_password) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { inState ->
            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        update_password_button.setOnClickListener {
            viewModel.setStateEvent(
                AccountStateEvent.ChangePasswordEvent(
                    input_current_password.text.toString(),
                    input_new_password.text.toString(),
                    input_confirm_new_password.text.toString()
                )
            )
        }
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiCommunicationListener.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let {
                if(stateMessage.response.message.equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)){
                    findNavController().popBackStack()
                }
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.removeStateMessage()
                        }
                    }
                )
            }
        })
    }

    companion object{
        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"
    }
}