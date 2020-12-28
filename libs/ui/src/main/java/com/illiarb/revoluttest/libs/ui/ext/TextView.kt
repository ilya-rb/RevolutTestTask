package com.illiarb.revoluttest.libs.ui.ext

import android.widget.TextView
import com.illiarb.revoluttest.libs.ui.common.Text

fun TextView.fromText(text: Text) {
    when (text) {
        is Text.ResourceString -> setText(text.id)
        is Text.StringText -> setText(text.text)
    }.exhaustive
}