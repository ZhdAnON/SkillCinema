package com.zhdanon.skillcinema.presentation.adapters.viewHolders

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.core.extensions.loadImage
import com.zhdanon.skillcinema.databinding.ItemStaffBinding
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes

class ItemStaffViewHolder(private val binding: ItemStaffBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: MyAdapterTypes.ItemMovieStaff, onStaffClick: (staffId: Int) -> Unit) {
        binding.apply {
            staffAvatar.loadImage(item.person.posterUrl)
            if (item.person.name != null) staffName.text = item.person.name
            if (item.person.description != null) staffRole.text = item.person.description
        }
        binding.root.setOnClickListener {
            onStaffClick(item.person.staffId)
            Toast.makeText(it.context, "${item.person.staffId} - ${item.person.name}", Toast.LENGTH_SHORT).show()
        }
    }
}