package com.zhdanon.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.databinding.ItemImageMovieDetailBinding
import com.zhdanon.skillcinema.databinding.ItemMovieBinding
import com.zhdanon.skillcinema.databinding.ItemStaffBinding
import com.zhdanon.skillcinema.presentation.adapters.viewHolders.ItemImageMovieDetailViewHolder
import com.zhdanon.skillcinema.presentation.adapters.viewHolders.ItemMovieViewHolder
import com.zhdanon.skillcinema.presentation.adapters.viewHolders.ItemStaffViewHolder

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
                    .bindItem(getItem(position) as MyAdapterTypes.ItemImage)
            }

            else -> throw Exception("Unknown ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyAdapterTypes.ItemMovie -> R.layout.item_movie
            is MyAdapterTypes.ItemMovieStaff -> R.layout.item_staff
            is MyAdapterTypes.ItemImage -> R.layout.item_image_movie_detail
            else -> throw Exception("Unknown ViewType")
        }
    }
}