package com.zhdanon.skillcinema.app.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemGalleryViewHolder
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemImageMovieDetailViewHolder
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemMovieViewHolder
import com.zhdanon.skillcinema.app.presentation.adapters.viewHolders.ItemStaffViewHolder
import com.zhdanon.skillcinema.databinding.ItemImageGalleryBinding
import com.zhdanon.skillcinema.databinding.ItemImageMovieDetailBinding
import com.zhdanon.skillcinema.databinding.ItemMovieBinding
import com.zhdanon.skillcinema.databinding.ItemStaffBinding

class MyListAdapter(
    private val maxListSize: Int?,
    private val clickEndButton: () -> Unit,
    private val clickItem: (itemId: Int) -> Unit
) : ListAdapter<MyAdapterTypes, RecyclerView.ViewHolder>(MyDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_movie -> {
                ItemMovieViewHolder(
                    ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_staff -> {
                ItemStaffViewHolder(
                    ItemStaffBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            R.layout.item_image_movie_detail ->
                ItemImageMovieDetailViewHolder(
                    ItemImageMovieDetailBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            R.layout.item_image_gallery ->
                ItemGalleryViewHolder(
                    ItemImageGalleryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            else -> throw Exception("Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.item_movie -> {
                if (maxListSize != null) {
                    val tempSize = if (itemCount <= maxListSize) itemCount else maxListSize + 1
                    if (position < tempSize - 1) {
                        (holder as ItemMovieViewHolder)
                            .bindItem(getItem(position) as MyAdapterTypes.ItemMovie) { clickItem(it) }
                    } else (holder as ItemMovieViewHolder).bindNextShow(clickEndButton)
                } else (holder as ItemMovieViewHolder)
                    .bindItem(getItem(position) as MyAdapterTypes.ItemMovie) { clickItem(it) }
            }

            R.layout.item_staff -> {
                (holder as ItemStaffViewHolder)
                    .bindItem(getItem(position) as MyAdapterTypes.ItemMovieStaff) { clickItem(it) }
            }

            R.layout.item_image_movie_detail -> {
                (holder as ItemImageMovieDetailViewHolder)
                    .bindItem(getItem(position) as MyAdapterTypes.ItemMovieDetailImage)
            }

            R.layout.item_image_gallery -> {
                (holder as ItemGalleryViewHolder)
                    .bindItem(getItem(position) as MyAdapterTypes.ItemImage)
            }

            else -> throw Exception("Unknown ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyAdapterTypes.ItemMovie -> R.layout.item_movie
            is MyAdapterTypes.ItemMovieStaff -> R.layout.item_staff
            is MyAdapterTypes.ItemMovieDetailImage -> R.layout.item_image_movie_detail
            is MyAdapterTypes.ItemImage -> R.layout.item_image_gallery
            else -> throw Exception("Unknown ViewType")
        }
    }
}