package com.zhdanon.skillcinema.app.presentation.profile

import android.view.LayoutInflater
import com.zhdanon.skillcinema.app.core.BaseFragment
import com.zhdanon.skillcinema.databinding.FragmentProfileBinding

class FragmentProfile : BaseFragment<FragmentProfileBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentProfileBinding.inflate(layoutInflater)
}