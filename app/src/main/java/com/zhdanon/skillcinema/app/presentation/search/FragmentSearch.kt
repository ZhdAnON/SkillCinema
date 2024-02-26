package com.zhdanon.skillcinema.app.presentation.search

import android.view.LayoutInflater
import com.zhdanon.skillcinema.app.core.BaseFragment
import com.zhdanon.skillcinema.databinding.FragmentSearchBinding

class FragmentSearch : BaseFragment<FragmentSearchBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentSearchBinding.inflate(layoutInflater)
}