package com.example.faircon.framework.presentation.ui.auth.passwordReset

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.faircon.business.domain.util.Urls
import com.example.faircon.framework.presentation.navigation.AuthScreen
import com.example.faircon.framework.presentation.ui.auth.AuthViewModel
import com.example.faircon.framework.presentation.ui.auth.passwordReset.WebAppInterface.OnWebInteractionCallback

@Composable
fun PasswordResetScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val viewState = viewModel.viewState.collectAsState()

    WebViewComposable(
        webInteractionCallback = viewModel.webInteractionCallback,
        showProgressbar = { viewModel.shouldDisplayProgressBar.value = it }
    )

    if (viewState.value.resetPasswordSuccess == true){
        navController.navigate(AuthScreen.PasswordResetSuccessScreen.route){
            popUpTo = navController.graph.startDestination
            launchSingleTop = true
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComposable(
    webInteractionCallback: OnWebInteractionCallback,
    showProgressbar: (Boolean) -> Unit
) {
    return AndroidView(factory = { context ->
        WebView(context).apply {

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            showProgressbar(true)

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    showProgressbar(false)
                }
            }

            loadUrl(Urls.PASSWORD_RESET_URL)

            settings.javaScriptEnabled = true

            addJavascriptInterface(
                WebAppInterface(webInteractionCallback),
                "AndroidTextListener"
            )
        }
    })
}