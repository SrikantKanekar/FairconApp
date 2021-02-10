package com.example.faircon.framework.presentation.ui.main.account

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import com.example.faircon.business.domain.state.StateMessageCallback
import com.example.faircon.framework.presentation.components.MyButton
import com.example.faircon.framework.presentation.components.MyCircularImage
import com.example.faircon.framework.presentation.components.MyLinkTextButton
import com.example.faircon.framework.presentation.components.ProfileDetailText
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.main.account.state.AccountStateEvent.GetAccountPropertiesEvent

class AccountFragment : BaseAccountFragment() {

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
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value
                ) {

                    val accountProperties = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .accountProperties

                    Scaffold {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Spacer(modifier = Modifier.height(20.dp))
                                MyCircularImage(imageVector = Icons.Default.Person)
                                Spacer(modifier = Modifier.height(20.dp))

                                Divider()

                                Spacer(modifier = Modifier.height(20.dp))

                                accountProperties?.let { accountProperties ->

                                    ProfileDetail(
                                        field = "Email",
                                        value = accountProperties.email,
                                        imageVector = Icons.Default.Email
                                    )

                                    ProfileDetail(
                                        field = "Username",
                                        value = accountProperties.username,
                                        imageVector = Icons.Default.Person
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 15.dp)
                            ) {
                                MyLinkTextButton(
                                    text = "Change Password",
                                    onClick = {
                                        findNavController()
                                            .navigate(R.id.action_accountFragment_to_changePasswordFragment)
                                    }
                                )

                                MyButton(
                                    text = "Logout",
                                    onClick = { viewModel.logout() }
                                )
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
        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
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
    field: String,
    value: String,
    imageVector: ImageVector
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            modifier = Modifier.padding(5.dp).preferredSize(30.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileDetailText(text = field)
            ProfileDetailText(text = value)
        }
    }
}