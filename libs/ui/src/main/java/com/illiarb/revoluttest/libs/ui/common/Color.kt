package com.illiarb.revoluttest.libs.ui.common

sealed class Color {
    data class ResourceColor(val id: Int) : Color()
}