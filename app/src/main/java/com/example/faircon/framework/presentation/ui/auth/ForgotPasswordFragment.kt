package com.example.faircon.framework.presentation.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.business.domain.state.MessageType
import com.example.faircon.business.domain.state.Response
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.business.domain.state.UIComponentType
import com.example.faircon.business.domain.util.Urls
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.presentation.ui.auth.ForgotPasswordFragment.WebAppInterface.OnWebInteractionCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class ForgotPasswordFragment : BaseAuthFragment() {

    lateinit var webView: WebView
    lateinit var progressbar: ProgressBar
    lateinit var returnButton: TextView
    lateinit var parentView: FrameLayout
    lateinit var suscessContainer: LinearLayout

    private val webInteractionCallback = object : OnWebInteractionCallback {

        override fun onError(errorMessage: String) {
            printLogD("ForgotPasswordFragment", "onError: $errorMessage")
            uiCommunicationListener.onResponseReceived(
                response = Response(
                    message = errorMessage,
                    uiComponentType = UIComponentType.Dialog,
                    messageType = MessageType.Error
                ),
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.removeStateMessage()
                    }
                }
            )
        }

        override fun onSuccess(email: String) {
            printLogD("ForgotPasswordFragment", "onSuccess: a reset link will be sent to $email.")
            onPasswordResetLinkSent()
        }

        override fun onLoading(isLoading: Boolean) {
            printLogD("ForgotPasswordFragment", "onLoading... ")

            //Removed because the website has its progress bar
            //viewModel.shouldDisplayProgressBar.value = isLoading
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.webView)
        progressbar = view.findViewById(R.id.progress_bar)
        returnButton = view.findViewById(R.id.return_to_launcher_fragment)
        parentView = view.findViewById(R.id.parent_view)
        suscessContainer = view.findViewById(R.id.password_reset_done_container)

        returnButton.setOnClickListener {
            findNavController().popBackStack()
        }
        loadPasswordResetWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPasswordResetWebView() {
        progressbar.visibility = View.VISIBLE

        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressbar.visibility = View.INVISIBLE
                }
            }
            loadUrl(Urls.PASSWORD_RESET_URL)
            settings.javaScriptEnabled = true
            addJavascriptInterface(
                WebAppInterface(webInteractionCallback),
                "AndroidTextListener"
            )
        }
    }

    class WebAppInterface
    constructor(
        private val callback: OnWebInteractionCallback
    ) {
        @JavascriptInterface
        fun onSuccess(email: String) {
            callback.onSuccess(email)
        }

        @JavascriptInterface
        fun onError(errorMessage: String) {
            callback.onError(errorMessage)
        }

        @JavascriptInterface
        fun onLoading(isLoading: Boolean) {
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)
            fun onError(errorMessage: String)
            fun onLoading(isLoading: Boolean)
        }
    }

    fun onPasswordResetLinkSent() {
        CoroutineScope(Main).launch {
            parentView.removeView(webView)
            webView.destroy()

            val animation = TranslateAnimation(
                suscessContainer.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            suscessContainer.startAnimation(animation)
            suscessContainer.visibility = View.VISIBLE
        }
    }
}


// WebView Not Working well with jetpack Compose
// Will be implemented in future

//@AndroidEntryPoint
//class ForgotPasswordFragment : BaseAuthFragment() {
//
//    private val webInteractionCallback = object : OnWebInteractionCallback {
//
//        override fun onError(errorMessage: String) {
//            printLogD("ForgotPasswordFragment", "onError: $errorMessage")
//            uiCommunicationListener.onResponseReceived(
//                response = Response(
//                    message = errorMessage,
//                    uiComponentType = UIComponentType.Dialog,
//                    messageType = MessageType.Error
//                ),
//                stateMessageCallback = object : StateMessageCallback {
//                    override fun removeMessageFromStack() {
//                        viewModel.removeStateMessage()
//                    }
//                }
//            )
//        }
//
//        override fun onSuccess(email: String) {
//            printLogD("ForgotPasswordFragment", "onSuccess: a reset link will be sent to $email.")
//            viewModel.webViewSuccess.value = true
//        }
//
//        override fun onLoading(isLoading: Boolean) {
//            printLogD("ForgotPasswordFragment", "onLoading... ")
//            //Removed because the website has its progress bar
//            //uiCommunicationListener.displayProgressBar(isLoading)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                FairconTheme(
//                    displayProgressBar = viewModel.shouldDisplayProgressBar.value,
//                    displayLogo = false
//                ) {
//
//                    if (viewModel.webViewSuccess.value) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(top = 30.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Top,
//                        ) {
//
//                            Text(
//                                text = "Password reset email sent",
//                                style = MaterialTheme.typography.h5,
//                                color = MaterialTheme.colors.onBackground
//                            )
//
//                            Text(
//                                modifier = Modifier.padding(top = 20.dp),
//                                text = "We\\'ve emailed you instructions for setting your password. If an account exists\n" +
//                                        "        with the email you entered.\n" +
//                                        "        You should receive them shortly.If you don\\'t receive an email, please make sure you\\'ve entered the address you\n" +
//                                        "        registered with,\n" +
//                                        "        and check your spam folder.",
//                                style = MaterialTheme.typography.body1,
//                                color = MaterialTheme.colors.onBackground
//                            )
//
//                            TextButton(
//                                modifier = Modifier.padding(top = 15.dp),
//                                onClick = { findNavController().popBackStack() }
//                            ) {
//                                Text(
//                                    text = "Return",
//                                    style = MaterialTheme.typography.overline,
//                                    color = linkColour
//                                )
//                            }
//                        }
//                    } else {
//                        WebViewComposable(
//                            webInteractionCallback = webInteractionCallback,
//                            showProgressbar = {
//                                viewModel.shouldDisplayProgressBar.value = it
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    class WebAppInterface
//    constructor(
//        private val callback: OnWebInteractionCallback
//    ) {
//        @JavascriptInterface
//        fun onSuccess(email: String) {
//            callback.onSuccess(email)
//        }
//
//        @JavascriptInterface
//        fun onError(errorMessage: String) {
//            callback.onError(errorMessage)
//        }
//
//        @JavascriptInterface
//        fun onLoading(isLoading: Boolean) {
//            callback.onLoading(isLoading)
//        }
//
//        interface OnWebInteractionCallback {
//            fun onSuccess(email: String)
//            fun onError(errorMessage: String)
//            fun onLoading(isLoading: Boolean)
//        }
//    }
//
//    fun onPasswordResetLinkSent() {
////        parent_view.removeView(webView)
////        webView.destroy()
////        CoroutineScope(Main).launch {
////            parent_view.removeView(webView)
////            webView.destroy()
////
////            val animation = TranslateAnimation(
////                password_reset_done_container.width.toFloat(),
////                0f,
////                0f,
////                0f
////            )
////            animation.duration = 500
////            password_reset_done_container.startAnimation(animation)
////            password_reset_done_container.visibility = View.VISIBLE
////        }
//    }
//}
//
//@SuppressLint("SetJavaScriptEnabled")
//@Composable
//fun WebViewComposable(
//    webInteractionCallback: OnWebInteractionCallback,
//    showProgressbar: (Boolean) -> Unit
//) {
//    return AndroidView(viewBlock = { context ->
//        WebView(context).apply {
//
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//
//            showProgressbar(true)
//            webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    showProgressbar(false)
//                }
//            }
//
//            loadUrl(Urls.PASSWORD_RESET_URL)
//
//            settings.javaScriptEnabled = true
//
//            addJavascriptInterface(
//                ForgotPasswordFragment.WebAppInterface(webInteractionCallback),
//                "AndroidTextListener"
//            )
//        }
//    })
//}