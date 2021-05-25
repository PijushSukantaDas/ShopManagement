package com.example.shopmanagement.ui.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:loadImage")
fun ImageView.loadImage(image: Uri) {
    Glide.with(this)
        .load(image)
        .into(this)
}