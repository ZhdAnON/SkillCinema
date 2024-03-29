package com.zhdanon.skillcinema.app.core

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zhdanon.skillcinema.R

fun ImageView.loadImage(imageUrl: String) {
    Glide
        .with(this)
        .load(imageUrl)
        .placeholder(R.drawable.no_poster)
        .into(this)
}