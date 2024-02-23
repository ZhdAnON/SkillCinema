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
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.core.BaseFragment
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.databinding.FragmentGalleryBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.presentation.adapters.MyPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentGallery : BaseFragment<FragmentGalleryBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentGalleryBinding.inflate(layoutInflater)

    private val viewModel: ViewModelGallery by viewModels()

    private lateinit var imagesPagingAdapter: MyPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentGalleryArgs by navArgs()

        setLoadingState()
        viewModel.setGallery(args.movieId)
        setAdapter()

        binding.incProgress.loadingRefreshBtn.setOnClickListener {
            viewModel.setChipGroup(args.movieId)
            viewModel.setGallery(args.movieId)
            imagesPagingAdapter.refresh()
        }
        binding.galleryBackBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadState.collect { state ->
                    when (state) {
                        StateLoading.Loading -> {
                            setVisibilityViews(
                                progressBanner = true,
                                progressBtn = false,
                                progressBar = true,
                                dataList = false
                            )
                        }
                        StateLoading.Success -> {
                            setVisibilityViews(
                                progressBanner = false,
                                progressBtn = false,
                                progressBar = false,
                                dataList = true
                            )
                            setChipButton()
                            setGalleryImages()
                        }
                        else -> {
                            setVisibilityViews(
                                progressBanner = true,
                                progressBtn = true,
                                progressBar = false,
                                dataList = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        imagesPagingAdapter = MyPagingAdapter { }
        binding.galleryList.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                .apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position % 3 == 0) 2 else 1
                        }
                    }
                }
        binding.galleryList.adapter = imagesPagingAdapter

        imagesPagingAdapter.addLoadStateListener { state ->
            val currentState = state.refresh
            setVisibilityViews(
                progressBanner = currentState == LoadState.Loading,
                progressBar = currentState == LoadState.Loading,
                progressBtn = currentState != LoadState.Loading,
                dataList = currentState != LoadState.Loading
            )

            when (currentState) {
                is LoadState.Loading -> {
                    setVisibilityViews(
                        progressBanner = true,
                        progressBar = true,
                        progressBtn = false,
                        dataList = false
                    )
                }

                is LoadState.NotLoading -> {
                    setVisibilityViews(
                        progressBanner = false,
                        progressBar = false,
                        progressBtn = false,
                        dataList = true
                    )
                }

                else -> {
                    binding.incProgress.root.isVisible = true
                    setVisibilityViews(
                        progressBanner = true,
                        progressBar = false,
                        progressBtn = true,
                        dataList = false
                    )
                }
            }
        }
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
                                viewModel.updateGallery(category = myChip.transitionName)
                                imagesPagingAdapter.refresh()
                            }
                            if (chipGroup.size == 0) {
                                chip.isChecked = true
                                viewModel.updateGallery(category = chip.transitionName)
                                imagesPagingAdapter.refresh()
                            }
                            chipGroup.addView(chip)
                        }
                    }
                    binding.galleryChipsGroupContainer.addView(chipGroup)
                }
            }
        }
    }

    private fun setGalleryImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imagesPaging.collect {
                    imagesPagingAdapter.submitData(it.map { image -> MyAdapterTypes.ItemImage(image) })
                }
            }
        }
    }

    private fun setVisibilityViews(
        progressBanner: Boolean,
        progressBtn: Boolean,
        progressBar: Boolean,
        dataList: Boolean
    ) {
        binding.apply {
            incProgress.loadingBanner.isVisible = progressBanner
            incProgress.loadingRefreshBtn.isVisible = progressBtn
            incProgress.loadingProgress.isVisible = progressBar

            galleryList.isVisible = dataList
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