package com.zhdanon.skillcinema.app.presentation.adapters.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.app.core.loadImage
import com.zhdanon.skillcinema.app.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.databinding.ItemImageMovieDetailBinding

class ItemImageMovieDetailViewHolder(
    private val binding: ItemImageMovieDetailBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemMovieDetailImage) {
        binding.apply {
            galleryImageFilmDetail.loadImage(item.image.previewUrl)
        }
    }
}