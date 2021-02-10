package com.example.faircon.framework.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import com.example.faircon.R
import com.example.faircon.framework.presentation.ui.BaseActivity
import com.example.faircon.framework.presentation.ui.main.MainActivity
import com.example.faircon.framework.presentation.ui.auth.state.AuthStateEvent.*
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.business.interactors.auth.CheckPreviousUser.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    lateinit var splashLogo: ImageView
    lateinit var fragmentContainerView: FragmentContainerView

    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        splashLogo = findViewById(R.id.splash_logo)
        fragmentContainerView = findViewById(R.id.auth_nav_host_fragment)

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

        viewModel.stateMessage.observe(this, { stateMessage ->
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

        sessionManager.cachedToken.observe(this, { token ->
            token.let { authToken ->
                if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                    navMainActivity()
                }
            }
        })
    }

    private fun onFinishCheckPreviousAuthUser() {
        fragmentContainerView.visibility = View.VISIBLE
        splashLogo.visibility = View.INVISIBLE
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}