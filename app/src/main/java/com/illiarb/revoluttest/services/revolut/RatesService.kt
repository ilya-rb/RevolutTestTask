package com.illiarb.revoluttest.services.revolut

import com.illiarb.revoluttest.services.revolut.entity.Rate
import io.reactivex.rxjava3.core.Flowable

interface RatesService {

    fun observeLatestRates(
        baseCurrency: String? = null,
        updateInterval: Long = DEFAULT_POLLING_RATE_MILLIS
    ): Flowable<LatestRates>

    data class LatestRates(val baseRate: Rate, val rates: List<Rate>)

    companion object {
        const val DEFAULT_POLLING_RATE_MILLIS = 1000L
    }
}