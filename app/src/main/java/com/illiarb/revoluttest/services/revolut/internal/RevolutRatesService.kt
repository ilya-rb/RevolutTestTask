package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.util.Optional
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesResponse
import com.illiarb.revoluttest.services.revolut.internal.cache.RatesCache
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class RevolutRatesService @Inject constructor(
    private val latestRatesApi: LatestRatesApi,
    private val imageUrlCreator: ImageUrlCreator,
    private val schedulerProvider: SchedulerProvider,
    private val ratesCache: RatesCache,
    private val connectivityStatus: ConnectivityStatus
) : RatesService {

    override fun observeLatestRates(
        baseCurrency: String?,
        updateInterval: Long
    ): Flowable<RatesService.LatestRates> {
        return Flowable.interval(
            updateInterval,
            TimeUnit.MILLISECONDS,
            schedulerProvider.computation
        )
            .withLatestFrom(
                connectivityStatus.connectivityStatus(),
                BiFunction { _: Long, state: State -> state }
            )
            .flatMap { state ->
                when (state) {
                    State.NOT_CONNECTED -> ratesCache.latestRates.flatMap {
                        when (it) {
                            is Optional.Some -> Flowable.just(it.element)
                            is Optional.None ->
                                Flowable.error(RuntimeException("Not connected to network"))
                        }.exhaustive
                    }
                    else -> latestRatesApi.latest(baseCurrency)
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
                        .doOnNext(ratesCache::storeLatestRates)
                }
            }
    }

    private fun LatestRatesResponse.asRatesList(): List<Rate> =
        rates.map {
            Rate(
                imageUrl = imageUrlCreator.createCountryFlagUrl(it.key),
                code = it.key,
                rate = it.value
            )
        }
}