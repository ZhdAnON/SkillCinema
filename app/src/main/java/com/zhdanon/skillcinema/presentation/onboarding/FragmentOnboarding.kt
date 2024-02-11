package com.zhdanon.skillcinema.presentation.onboarding

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOnboarding : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OnBoardingPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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