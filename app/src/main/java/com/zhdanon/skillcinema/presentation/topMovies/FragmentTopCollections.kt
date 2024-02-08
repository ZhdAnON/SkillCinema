package com.zhdanon.skillcinema.presentation.topMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhdanon.skillcinema.databinding.FragmentTopCollectionsBinding
import com.zhdanon.skillcinema.domain.CategoriesMovies
import com.zhdanon.skillcinema.presentation.adapters.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTopCollections : Fragment() {
    private var _binding: FragmentTopCollectionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelTopCollections by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopCollectionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCategories()
    }

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

    private fun onClickMovie(movieId: Int) {
        Toast.makeText(requireContext(), "movieId = $movieId", Toast.LENGTH_SHORT).show()
    }

    private fun onClickShowFullCollection(collection: CategoriesMovies) {
        Toast.makeText(requireContext(), "movieId = ${collection.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}