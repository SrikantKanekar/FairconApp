package com.example.faircon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.faircon.R
import com.example.faircon.ui.BaseActivity
import com.example.faircon.ui.main.MainActivity
import com.example.faircon.ui.auth.state.AuthStateEvent.*
import com.example.faircon.util.StateMessageCallback
import com.example.faircon.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
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
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        viewModel.numActiveJobs.observe(this, Observer {
            displayProgressBar(viewModel.areAnyJobsActive())
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
                            viewModel.clearStateMessage()
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

    private fun checkPreviousAuthUser() {
        viewModel.setStateEvent(CheckPreviousAuthEvent)
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }
}