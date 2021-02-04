package com.example.faircon.framework.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.faircon.R
import com.example.faircon.framework.presentation.auth.state.AuthStateEvent.RegisterAttemptEvent
import com.example.faircon.framework.presentation.auth.state.RegistrationFields
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment: BaseAuthFragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        register_button.setOnClickListener {
            register()
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let {fields->
                fields.registration_email?.let { input_email.setText(it) }
                fields.registration_username?.let { input_username.setText(it) }
                fields.registration_password?.let { input_password.setText(it) }
                fields.registration_confirm_password?.let { input_password_confirm.setText(it) }
            }
        })
    }

    private fun register() {
        viewModel.setStateEvent(
            RegisterAttemptEvent(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}