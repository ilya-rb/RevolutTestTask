package com.illiarb.revoluttest.libs.ui.common

sealed class Text {
    data class ResourceString(val id: Int) : Text()
}