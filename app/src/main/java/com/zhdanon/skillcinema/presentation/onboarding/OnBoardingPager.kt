package com.zhdanon.skillcinema.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.databinding.OnboardingItemPageBinding

class OnBoardingPager(
    private val introList: List<OnBoardingResources>
) : RecyclerView.Adapter<OnBoardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OnBoardingViewHolder(
        OnboardingItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.binding.apply {
            introImage.setImageResource(introList[position].imageId)
            introMessage.text = introList[position].message
        }
    }

    override fun getItemCount() = introList.size
}