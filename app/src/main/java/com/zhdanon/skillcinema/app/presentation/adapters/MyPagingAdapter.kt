package com.zhdanon.skillcinema.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemGalleryViewHolder
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemMovieViewHolder
import com.zhdanon.skillcinema.databinding.ItemImageGalleryBinding
import com.zhdanon.skillcinema.databinding.ItemMovieBinding

class MyPagingAdapter(
    private val onClick: (Int) -> Unit
) : PagingDataAdapter<MyAdapterTypes, RecyclerView.ViewHolder>(MyDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_movie -> {
                ItemMovieViewHolder(
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.item_image_gallery -> {
                ItemGalleryViewHolder(
                    ItemImageGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw Exception("onCreateViewHolder - Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemMovieViewHolder -> {
                val item = getItem(position) as MyAdapterTypes.ItemMovie
                holder.bindItem(item) { onClick(it) }
            }
            is ItemGalleryViewHolder -> {
                val item = getItem(position) as MyAdapterTypes.ItemImage
                holder.bindItem(item)
            }
            else -> throw Exception("onBindViewHolder - Unknown ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyAdapterTypes.ItemMovie -> R.layout.item_movie
            is MyAdapterTypes.ItemImage -> R.layout.item_image_gallery
            else -> 4
        }
    }
}