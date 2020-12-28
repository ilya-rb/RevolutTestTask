package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class RevolutRatesService @Inject constructor(
    private val latestRatesApi: LatestRatesApi
) : RatesService {

    override fun observeLatestRates(
        baseCurrency: String?,
        updateInterval: Long
    ): Flowable<RatesService.LatestRates> {
        return Flowable.interval(updateInterval, TimeUnit.MILLISECONDS)
            .flatMap { latestRatesApi.latest(baseCurrency) }
            .map {
                RatesService.LatestRates(
                    baseCurrency = it.baseCurrency,
                    rates = it.asRatesList()
                )
            }
    }
}