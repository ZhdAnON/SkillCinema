package com.zhdanon.skillcinema.app.presentation.topMovies

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zhdanon.skillcinema.databinding.FragmentTopCollectionsBinding
import com.zhdanon.skillcinema.app.core.BaseFragment
import com.zhdanon.skillcinema.app.core.StateLoading
import com.zhdanon.skillcinema.app.presentation.adapters.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTopCollections : BaseFragment<FragmentTopCollectionsBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentTopCollectionsBinding.inflate(inflater)

    private val viewModel: ViewModelTopCollections by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateLoadingListener()
        getCategories()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stateLoadingListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadState.collect { state ->
                    binding.incProgress.loadingRefreshBtn.setOnClickListener {
                        viewModel.getTopCollection()
                    }
                    when (state) {
                        is StateLoading.Loading -> {
                            setVisibilityViews(
                                progressBanner = true,
                                progressBtn = false,
                                progressBar = true,
                                dataViews = false
                            )
                        }

                        is StateLoading.Success -> {
                            setVisibilityViews(
                                progressBanner = false,
                                progressBtn = false,
                                progressBar = false,
                                dataViews = true
                            )
                        }

                        else -> {
                            setVisibilityViews(
                                progressBanner = true,
                                progressBtn = true,
                                progressBar = false,
                                dataViews = false
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topMovies.collect {
                    categoryAdapter = CategoryAdapter(
                        20,
                        it,
                        ::onClickShowFullCollection,
                        ::onClickMovie
                    )
                    binding.categoryList.adapter = categoryAdapter
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickMovie(movieId: Int) {
        val action =
            FragmentTopCollectionsDirections.actionFragmentTopCollectionsToFragmentDetailMovie(
                movieId
            )
        findNavController().navigate(action)
    }

    private fun onClickShowFullCollection(collection: String) {
        val action = FragmentTopCollectionsDirections
            .actionFragmentTopCollectionsToFragmentTopCollectionsFull(collection)
        findNavController().navigate(action)
    }

    private fun setVisibilityViews(
        progressBanner: Boolean,
        progressBtn: Boolean,
        progressBar: Boolean,
        dataViews: Boolean
    ) {
        binding.apply {
            incProgress.loadingBanner.isVisible = progressBanner
            incProgress.loadingRefreshBtn.isVisible = progressBtn
            incProgress.loadingProgress.isVisible = progressBar

            bannerAppName.isVisible = dataViews
            categoryList.isVisible = dataViews
        }
    }
}