package com.zhdanon.skillcinema.app.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.app.core.BaseFragment
import com.zhdanon.skillcinema.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun initBinding(inflater: LayoutInflater) = FragmentMainBinding.inflate(layoutInflater)

    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost =
            childFragmentManager.findFragmentById(R.id.working_container) as NavHostFragment
        navController = navHost.navController
    }
}