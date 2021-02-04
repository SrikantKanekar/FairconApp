package com.example.faircon.framework.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.faircon.R
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.main.MainActivity
import com.example.faircon.framework.presentation.auth.state.AuthStateEvent.*
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.business.interactors.auth.CheckPreviousUser.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(CheckPreviousAuthEvent)
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        viewModel.shouldDisplayProgressBar.observe(this, {
            displayProgressBar(it)
        })

        viewModel.stateMessage.observe(this, Observer { stateMessage ->
            stateMessage?.let {

                if (stateMessage.response.message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)) {
                    onFinishCheckPreviousAuthUser()
                }

                onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.removeStateMessage()
                        }
                    }
                )
            }
        })

        sessionManager.cachedToken.observe(this, Observer { token ->
            token.let { authToken ->
                if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousAuthUser() {
        auth_nav_host_fragment.visibility = View.VISIBLE
        splash_logo.visibility = View.INVISIBLE
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}