package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.modules.home.HomeViewModel.UiRate

data class RatesChangedPayload(
    val hasNewImage: Boolean = true,
    val hasNewRate: Boolean = true
) {

    companion object {

        fun create(old: UiRate, new: UiRate): RatesChangedPayload {
            return RatesChangedPayload(
                hasNewImage = old.imageUrl != new.imageUrl,
                hasNewRate = old.rate != new.rate
            )
        }
    }
}