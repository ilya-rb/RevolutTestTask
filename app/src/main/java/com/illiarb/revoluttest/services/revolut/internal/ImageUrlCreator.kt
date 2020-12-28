package com.illiarb.revoluttest.services.revolut.internal

import java.util.Locale
import javax.inject.Inject

class ImageUrlCreator @Inject constructor() {

    fun createCountryFlagUrl(code: String): String {
        val countryCode = code.take(2).toLowerCase(Locale.ROOT)
        return "https://www.countryflags.io/$countryCode/flat/$IMAGE_SIZE_PX.png"
    }

    companion object {
        const val IMAGE_SIZE_PX = 64
    }
}