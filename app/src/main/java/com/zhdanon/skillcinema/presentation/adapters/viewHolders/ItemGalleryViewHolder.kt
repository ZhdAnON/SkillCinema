package com.zhdanon.skillcinema.presentation.adapters.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.core.extensions.loadImage
import com.zhdanon.skillcinema.databinding.ItemImageGalleryBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes

class ItemGalleryViewHolder(
    private val binding: ItemImageGalleryBinding
) :RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemImage) {
        binding.galleryImage.loadImage(item.image.imageUrl)
    }
}