package com.zhdanon.skillcinema.presentation.topMoviesFullCollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.zhdanon.skillcinema.core.BaseFragment
import com.zhdanon.skillcinema.databinding.FragmentTopCollectionFullBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.presentation.adapters.MyPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTopCollectionsFull : BaseFragment<FragmentTopCollectionFullBinding>() {
    override fun initBinding(inflater: LayoutInflater) =
        FragmentTopCollectionFullBinding.inflate(inflater)

    private val viewModel: ViewModelTopCollectionFull by viewModels()
    private lateinit var categoryAdapter: MyPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentTopCollectionsFullArgs by navArgs()
        viewModel.setCategory(args.category)
        binding.fullCollectionCategoryTv.text = viewModel.getCollectionLabel(args.category)
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setAdapter()
        setMoviesList()
    }

    private fun setAdapter() {
        categoryAdapter = MyPagingAdapter { onClickMovie(it) }

        binding.fullCollectionList.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.fullCollectionList.adapter = categoryAdapter

        binding.incProgress.loadingRefreshBtn.setOnClickListener { categoryAdapter.retry() }

        categoryAdapter.addLoadStateListener { state ->
            val currentState = state.refresh
            binding.fullCollectionList.isVisible = currentState != LoadState.Loading
            binding.incProgress.root.isVisible = currentState == LoadState.Loading
            binding.incProgress.loadingRefreshBtn.isVisible = currentState != LoadState.Loading

            when (currentState) {
                is LoadState.Loading -> {
                    binding.fullCollectionList.isVisible = false
                    binding.incProgress.root.isVisible = true
                    binding.incProgress.loadingRefreshBtn.isVisible = false
                }

                is LoadState.NotLoading -> {
                    binding.fullCollectionList.isVisible = true
                    binding.incProgress.root.isVisible = false
                    binding.incProgress.loadingRefreshBtn.isVisible = true
                }

                else -> {
                    binding.fullCollectionList.isVisible = false
                    binding.incProgress.root.isVisible = false
                    binding.incProgress.loadingRefreshBtn.isVisible = true
                }
            }
        }
    }

    private fun setMoviesList() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topMoviesPaging.collect {
                    categoryAdapter.submitData(it.map { film -> MyAdapterTypes.ItemMovie(film) })
                }
            }
        }
    }

    private fun onClickMovie(movieId: Int) {
        val action =
            FragmentTopCollectionsFullDirections.actionFragmentTopCollectionsFullToFragmentDetailMovie(
                movieId
            )
        findNavController().navigate(action)
    }
}