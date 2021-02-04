package com.example.faircon.framework.presentation.main.account

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.faircon.framework.presentation.ui.UICommunicationListener
import com.example.faircon.business.domain.util.printLogD

abstract class BaseAccountFragment
constructor(
    @LayoutRes
    private val layoutRes: Int,
) : Fragment(layoutRes) {

    val viewModel: AccountViewModel by activityViewModels()
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            printLogD("BaseAccountFragment", "$context must implement UICommunicationListener")
        }
    }
}





