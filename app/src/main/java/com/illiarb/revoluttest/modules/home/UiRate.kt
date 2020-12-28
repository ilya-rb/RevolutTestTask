package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.libs.ui.common.Color

data class UiRate(
    val imageUrl: String,
    val code: String,
    val caption: String,
    val rate: String,
    val isBaseRate: Boolean,
    val background: Color?
)