package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesResponse
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class RevolutRatesService @Inject constructor(
    private val latestRatesApi: LatestRatesApi,
    private val imageUrlCreator: ImageUrlCreator
) : RatesService {

    override fun observeLatestRates(
        baseCurrency: String?,
        updateInterval: Long
    ): Flowable<RatesService.LatestRates> {
        return Flowable.interval(updateInterval, TimeUnit.MILLISECONDS)
            .flatMap { latestRatesApi.latest(baseCurrency) }
            .map {
                RatesService.LatestRates(
                    baseRate = Rate(
                        imageUrl = imageUrlCreator.createCountryFlagUrl(it.baseCurrency),
                        code = it.baseCurrency,
                        rate = 1f
                    ),
                    rates = it.asRatesList()
                )
            }
    }

    private fun LatestRatesResponse.asRatesList(): List<Rate> = rates.map {
        Rate(
            imageUrl = imageUrlCreator.createCountryFlagUrl(it.key),
            code = it.key,
            rate = it.value
        )
    }
}