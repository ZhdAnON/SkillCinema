package com.zhdanon.skillcinema.presentation.topMovies

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zhdanon.skillcinema.databinding.FragmentTopCollectionsBinding
import com.zhdanon.skillcinema.domain.CategoriesMovies
import com.zhdanon.skillcinema.core.BaseFragment
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.presentation.adapters.CategoryAdapter
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
                    when (state) {
                        is StateLoading.Loading -> {
                            binding.apply {
                                incProgress.loadingProgress.isVisible = true
                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = false

                                bannerAppName.isVisible = false
                                categoryList.isVisible = false
                            }
                        }

                        is StateLoading.Success -> {
                            binding.apply {
                                incProgress.loadingBanner.isVisible = false
                                incProgress.loadingRefreshBtn.isVisible = false
                                incProgress.loadingProgress.isVisible = false

                                bannerAppName.isVisible = true
                                categoryList.isVisible = true
                            }
                        }

                        else -> {
                            binding.apply {
                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = true
                                incProgress.loadingProgress.isVisible = false

                                incProgress.loadingRefreshBtn.setOnClickListener {
                                    viewModel.getTopCollection()
                                }

                                bannerAppName.isVisible = false
                                categoryList.isVisible = false
                            }
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
                        { onClickShowFullCollection(it) },
                        { onClickMovie(it) })
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

    private fun onClickShowFullCollection(collection: CategoriesMovies) {
        Toast.makeText(requireContext(), "movieId = ${collection.text}", Toast.LENGTH_SHORT).show()
    }
}