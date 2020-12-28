package com.illiarb.revoluttest.libs.ui.common

sealed class Text {
    data class ResourceString(val id: Int) : Text()
    data class StringText(val text: String) : Text()
}