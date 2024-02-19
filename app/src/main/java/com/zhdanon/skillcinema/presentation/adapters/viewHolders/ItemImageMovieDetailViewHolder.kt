package com.zhdanon.skillcinema.presentation.adapters.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.core.extensions.loadImage
import com.zhdanon.skillcinema.databinding.ItemImageMovieDetailBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes

class ItemImageMovieDetailViewHolder(
    private val binding: ItemImageMovieDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemMovieDetailImage) {
        binding.apply {
            galleryImageFilmDetail.loadImage(item.image.previewUrl)
        }
    }
}