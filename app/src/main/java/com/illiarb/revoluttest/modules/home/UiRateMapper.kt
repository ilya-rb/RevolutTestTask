package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.libs.ui.R
import com.illiarb.revoluttest.libs.ui.common.Color
import com.illiarb.revoluttest.services.revolut.entity.Rate
import java.text.DecimalFormat
import javax.inject.Inject

class UiRateMapper @Inject constructor() {

    private val rateDisplayFormatter = DecimalFormat(CURRENCY_RATE_FORMAT)

    fun fromRatesList(
        rates: List<Rate>,
        baseCurrency: String,
        baseRate: Float
    ) : List<UiRate> {
        return rates.map { rate ->
            val isBase = rate.code == baseCurrency

            UiRate(
                imageUrl = rate.imageUrl,
                code = rate.code,
                caption = rate.code,
                isBaseRate = isBase,
                rate = if (isBase) {
                    rateDisplayFormatter.format(baseRate)
                } else {
                    rateDisplayFormatter.format(baseRate * rate.rate)
                },
                background = if (isBase) {
                    Color.ResourceColor(R.color.revoluttest_grey_100)
                } else {
                    null
                }
            )
        }
    }

    companion object {
        const val CURRENCY_RATE_FORMAT = "#0.00"
    }
}