package com.noradev.recipeapp.ui.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}