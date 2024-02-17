package com.zhdanon.skillcinema.presentation.adapters.viewHolders

import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.databinding.ItemCategoryListBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.presentation.adapters.MyListAdapter
import com.zhdanon.skillcinema.presentation.topMovies.ViewModelTopCollections

class CategoryViewHolder(
    private val binding: ItemCategoryListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(
        maxListSize: Int,
        item: ViewModelTopCollections.Companion.HomeList,
        clickFullCollection: (category: String) -> Unit,
        clickMovie: (movieId: Int) -> Unit
    ) {
        val adapter =
            MyListAdapter(
                maxListSize,
                { clickFullCollection(item.categoryType) },
                { clickMovie(it) })
        adapter.submitList(item.filmList.map { MyAdapterTypes.ItemMovie(it) })
        binding.titleCategory.text = item.categoryLabel
        binding.filmList.adapter = adapter
        binding.tvTitleShowAll.apply {
            this.isInvisible = item.filmList.size < maxListSize
            this.setOnClickListener { clickFullCollection(item.categoryType) }
        }
    }
}