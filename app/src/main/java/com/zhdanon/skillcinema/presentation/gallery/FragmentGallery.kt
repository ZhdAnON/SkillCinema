package com.zhdanon.skillcinema.presentation.gallery

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.core.BaseFragment
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.databinding.FragmentGalleryBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.presentation.adapters.MyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentGallery : BaseFragment<FragmentGalleryBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentGalleryBinding.inflate(layoutInflater)

    private val viewModel: ViewModelGallery by viewModels()
    private lateinit var imagesAdapter: MyListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galleryBackBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val args: FragmentGalleryArgs by navArgs()
        viewModel.setChipGroup(args.movieId)

        stateLoadingListener()
        setAdapter()
        setChipButton()
    }

    private fun stateLoadingListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadState.collect { state ->
                    when (state) {
                        is StateLoading.Loading -> {
                            binding.apply {
                                incProgress.loadingProgress.isVisible = true
                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = false

                                galleryList.isVisible = false
                                galleryChipScrollGroup.isVisible = false
                            }
                        }

                        is StateLoading.Success -> {
                            binding.apply {
                                incProgress.loadingBanner.isVisible = false
                                incProgress.loadingRefreshBtn.isVisible = false
                                incProgress.loadingProgress.isVisible = false

                                galleryList.isVisible = true
                                galleryChipScrollGroup.isVisible = true
                            }
                        }

                        else -> {
                            binding.apply {
                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = true
                                incProgress.loadingProgress.isVisible = false

                                galleryList.isVisible = false
                                galleryChipScrollGroup.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        val gridManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                .apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position % 3 == 0) 2 else 1
                        }
                    }
                }

        imagesAdapter = MyListAdapter(20, {}) { }
        binding.galleryList.layoutManager = gridManager
        binding.galleryList.adapter = imagesAdapter
    }

    private fun setChipButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.galleryChipList.collect {
                    val chipGroup = ChipGroup(requireContext()).apply {
                        isSingleSelection = true
                        chipSpacingHorizontal = 8
                    }
                    it.forEach { (key, value) ->
                        if (value != 0) {
                            val nameChip = viewModel.getAppResources().getCategoryImages(key)
                            val chip = Chip(requireContext()).apply {
                                text = resources.getString(R.string.chip_name, nameChip, value)
                                chipBackgroundColor = chipBackColors
                                setTextColor(chipTextColors)
                                chipStrokeColor = chipStrokeColors
                                isCheckable = true
                                checkedIcon = null
                                transitionName = key
                                chipStrokeWidth = 1f
                                isSelected = false
                            }
                            chip.setOnClickListener { myChip ->
                                viewModel.updateGalleryType(category = myChip.transitionName)
                                binding.galleryList.invalidate()
                            }
                            if (chipGroup.size == 0) {
                                chip.isChecked = true
                                setGalleryImages(chip.transitionName)
                                binding.galleryList.invalidate()
                            }
                            chipGroup.addView(chip)
                        }
                    }
                    binding.galleryChipsGroupContainer.addView(chipGroup)
                }
            }
        }
    }

    private fun setGalleryImages(category: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateGalleryType(category)
                viewModel.images.collect {
                    imagesAdapter.submitList(it.map { image -> MyAdapterTypes.ItemImage(image) })
                }
            }
        }
    }

    companion object {
        val chipBackColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled),
                intArrayOf()
            ),
            intArrayOf(Color.BLUE, Color.WHITE)
        )
        val chipTextColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled),
                intArrayOf()
            ),
            intArrayOf(Color.WHITE, Color.BLACK)
        )
        val chipStrokeColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled),
                intArrayOf()
            ),
            intArrayOf(Color.BLUE, Color.BLACK)
        )
    }
}