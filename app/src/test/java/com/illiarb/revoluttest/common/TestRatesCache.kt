package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.util.Optional
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.internal.cache.RatesCache
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject

class TestRatesCache : RatesCache {

    override val latestRates: Flowable<Optional<RatesService.LatestRates>>
        get() = PublishSubject.create<Optional<RatesService.LatestRates>>()
            .toFlowable(BackpressureStrategy.LATEST)
            .startWithItem(Optional.None)

    override fun storeLatestRates(rates: RatesService.LatestRates) = Unit

    override fun clearCache() = Unit
}