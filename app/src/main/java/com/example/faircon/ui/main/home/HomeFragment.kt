package com.example.faircon.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.faircon.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment :Fragment(R.layout.fragment_home){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_controller.setOnClickListener {
            findNavController().navigate(R.id.controllerFragment)
        }
    }
}