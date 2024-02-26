package com.zhdanon.skillcinema.app.presentation.adapters.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.app.core.loadImage
import com.zhdanon.skillcinema.app.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.databinding.ItemImageGalleryBinding

class ItemGalleryViewHolder(
    private val binding: ItemImageGalleryBinding
) :RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: MyAdapterTypes.ItemImage) {
        binding.galleryImage.loadImage(item.image.imageUrl)
    }
}