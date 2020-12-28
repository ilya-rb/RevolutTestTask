package com.illiarb.revoluttest.services.revolut.internal.api

import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.math.round

@JsonClass(generateAdapter = true)
internal data class LatestRatesResponse(
    @Json(name = "baseCurrency") val baseCurrency: String,
    @Json(name = "rates") val rates: Map<String, Double>
) {

    fun asRatesList(): List<Rate> = rates.map {
        Rate(it.key, it.value.round(decimals = 2).toFloat())
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}