package com.zhdanon.skillcinema.presentation.onboarding

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.databinding.FragmentOnboardingBinding
import com.zhdanon.skillcinema.core.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOnboarding : BaseFragment<FragmentOnboardingBinding>() {
    override fun initBinding(inflater: LayoutInflater) = FragmentOnboardingBinding.inflate(inflater)

    private lateinit var adapter: OnBoardingPager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listIntro = listOf(
            OnBoardingResources(
                message = resources.getString(R.string.onboarding_first_description),
                imageId = R.drawable.wfh_2
            ),
            OnBoardingResources(
                message = resources.getString(R.string.onboarding_description_second),
                imageId = R.drawable.wfh_4_1
            ), OnBoardingResources(
                message = resources.getString(R.string.onboarding_description_third),
                imageId = R.drawable.wfh_8
            )
        )

        adapter = OnBoardingPager(listIntro)
        binding.introViewpager.adapter = adapter
        TabLayoutMediator(binding.tab, binding.introViewpager) { _, _ -> }.attach()

        binding.btnIntroSkip.setOnClickListener { skipIntroClick() }
    }

    private fun skipIntroClick() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putBoolean(PREFERENCES_NAME, true)
            apply()
        }
        findNavController().navigate(R.id.action_fragmentOnboarding_to_fragmentTopCollections)
    }

    companion object {
        const val PREFERENCES_NAME = "pref_name"
    }
}