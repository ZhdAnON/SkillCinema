package com.zhdanon.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.databinding.ItemMovieBinding
import com.zhdanon.skillcinema.presentation.adapters.viewHolders.ItemMovieViewHolder

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
            else -> throw Exception("onCreateViewHolder - Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemMovieViewHolder -> {
                val item = getItem(position) as MyAdapterTypes.ItemMovie
                holder.bindItem(item) { onClick(it) }
            }
            else -> throw Exception("onBindViewHolder - Unknown ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyAdapterTypes.ItemMovie -> R.layout.item_movie
            else -> 4
        }
    }
}