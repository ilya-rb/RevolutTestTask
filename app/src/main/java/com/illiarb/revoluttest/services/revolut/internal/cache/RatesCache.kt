package com.illiarb.revoluttest.services.revolut.internal.cache

import com.illiarb.revoluttest.libs.util.Optional
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import io.reactivex.rxjava3.core.Flowable

interface RatesCache {

    val latestRates: Flowable<Optional<LatestRates>>

    fun storeLatestRates(rates: LatestRates)

    fun clearCache()
}