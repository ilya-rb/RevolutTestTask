package com.illiarb.revoluttest.libs.ui.ext

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getColorAttr(@AttrRes id: Int): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(id, outValue, true)
    return outValue.data
}