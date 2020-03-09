package com.apdallahy3.accenturetask.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.apdallahy3.accenturetask.R
import com.apdallahy3.accenturetask.data.source.remote.ApiConstants
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

@BindingAdapter("iconID", "error")
fun loadImage(view: ImageView, icon: String?, error: Drawable) {
    icon?.let {
        val url = ApiConstants.ICON_URL_PREFIX + it + ApiConstants.ICON_URL_SUFFIX
        Glide.with(view.context).load(url).placeholder(R.drawable.loading).error(error).into(view)
    }

}