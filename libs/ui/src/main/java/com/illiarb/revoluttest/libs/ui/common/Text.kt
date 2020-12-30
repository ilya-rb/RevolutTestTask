package com.illiarb.revoluttest.libs.ui.common

import androidx.annotation.StringRes

sealed class Text {
    data class ResourceIdText(@StringRes val id: Int) : Text()
}