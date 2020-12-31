package com.illiarb.revoluttest.libs.ui.ext

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

fun EditText.hideKeyboard() {
    context.getSystemService<InputMethodManager>()?.let { imm ->
        windowToken?.let { token ->
            imm.hideSoftInputFromWindow(token, 0)
        }
    }
}