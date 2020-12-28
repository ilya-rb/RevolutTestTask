package com.illiarb.revoluttest.libs.ui.image

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .asBitmap()
            .optionalCenterCrop()
            .load(url)
            .into(imageView)
    }
}