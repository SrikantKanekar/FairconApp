package com.example.faircon.ui.main.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.faircon.R
import com.example.faircon.ui.UICommunicationListener
import com.example.faircon.util.Constants
import kotlinx.coroutines.*

abstract class BaseAccountFragment
constructor(
    @LayoutRes
    private val layoutRes: Int,
): Fragment(layoutRes){

    val viewModel: AccountViewModel by activityViewModels()
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





