package com.illiarb.revoluttest.libs.ui.ext

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.illiarb.revoluttest.libs.ui.common.Color

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setBackgroundColor(color: Color?) {
    background = when (color) {
        is Color.ResourceColor -> ColorDrawable(ContextCompat.getColor(context, color.id))
        else -> null
    }
}