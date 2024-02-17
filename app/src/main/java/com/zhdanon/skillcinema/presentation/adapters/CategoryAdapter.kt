package com.zhdanon.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.databinding.ItemCategoryListBinding
import com.zhdanon.skillcinema.presentation.adapters.viewHolders.CategoryViewHolder
import com.zhdanon.skillcinema.presentation.topMovies.ViewModelTopCollections

class CategoryAdapter(
    private val maxListSize: Int,
    private val category: List<ViewModelTopCollections.Companion.HomeList>,
    private val clickFullCollection: (collectionName: String) -> Unit,
    private val clickMovie: (movieId: Int) -> Unit
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        ItemCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindItem(
            maxListSize,
            category[position],
            { clickFullCollection(it) },
            { clickMovie(it) }
        )
    }

    override fun getItemCount() = category.size
}