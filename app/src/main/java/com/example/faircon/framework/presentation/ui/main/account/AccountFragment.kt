package com.example.faircon.framework.presentation.ui.main.account

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.framework.presentation.components.MyButton
import com.example.faircon.framework.presentation.components.image.MyCircularImage
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.components.ProfileDetailText
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent.GetAccountPropertiesEvent
import kotlinx.coroutines.launch

class AccountFragment : BaseAccountFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val scaffoldState = rememberScaffoldState()

                FairconTheme(
                    darkTheme = true,
                    isNetworkAvailable = true,
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value,
                    scaffoldState = scaffoldState,
                    messageQueue = viewModel.messageQueue.value,
                    onDismiss = viewModel::removeHeadMessage
                ) {

                    val accountProperties = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .accountProperties


                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {

                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Spacer(modifier = Modifier.height(30.dp))

                                MyCircularImage(
                                    modifier = Modifier.padding(start = 20.dp),
                                    imageVector = Icons.Default.Person
                                )

                                Spacer(modifier = Modifier.height(30.dp))

                                Divider()

                                Spacer(modifier = Modifier.height(30.dp))

                                accountProperties?.let { accountProperties ->

                                    ProfileDetail(
                                        field = "Email",
                                        value = accountProperties.email,
                                        imageVector = Icons.Default.Email
                                    )

                                    ProfileDetail(
                                        modifier = Modifier.padding(top = 15.dp),
                                        field = "Username",
                                        value = accountProperties.username,
                                        imageVector = Icons.Default.Person
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                MyLinkTextButton(
                                    text = "Change Password",
                                    onClick = {
                                        findNavController()
                                            .navigate(R.id.action_accountFragment_to_changePasswordFragment)
                                    }
                                )

                                MyButton(
                                    modifier = Modifier.padding(top = 0.dp).padding(bottom = 40.dp),
                                    text = "Logout",
                                    onClick = { viewModel.logout() }
                                )

                                if (false){
                                    viewModel.snackbarController.getScope().launch {
                                        viewModel.snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "An error occurred with this recipe",
                                            actionLabel = "Ok"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(GetAccountPropertiesEvent)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

@Composable
fun ProfileDetail(
    modifier: Modifier = Modifier,
    field: String,
    value: String,
    imageVector: ImageVector
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            modifier = Modifier.padding(10.dp).preferredSize(40.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        Column {
            ProfileDetailText(text = field)
            ProfileDetailText(text = value)
        }
    }
}