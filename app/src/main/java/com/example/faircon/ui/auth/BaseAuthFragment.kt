package com.example.faircon.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.faircon.ui.UICommunicationListener
import com.example.faircon.util.Constants

abstract class BaseAuthFragment
constructor(
    @LayoutRes
    private val layoutRes: Int,
): Fragment(layoutRes){

    val viewModel: AuthViewModel by activityViewModels()
    lateinit var uiCommunicationListener: UICommunicationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChannel()
    }

    private fun setupChannel() = viewModel.setupChannel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(Constants.TAG, "$context must implement UICommunicationListener" )
        }
    }
}














