package com.example.findhomes.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (url != null) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        }
    }
}