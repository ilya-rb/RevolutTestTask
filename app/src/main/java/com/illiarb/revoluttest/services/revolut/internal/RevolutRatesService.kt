package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import com.illiarb.revoluttest.libs.tools.ResourceResolver
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.util.Optional
import com.illiarb.revoluttest.libs.util.Result
import com.illiarb.revoluttest.network.ApiError
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
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
    private val connectivityStatus: ConnectivityStatus,
    private val resourceResolver: ResourceResolver
) : RatesService {

    override fun observeLatestRates(
        baseCurrency: String?,
        updateInterval: Long
    ): Flowable<Result<LatestRates>> {
        return Flowable.interval(
            updateInterval,
            TimeUnit.MILLISECONDS,
            schedulerProvider.computation
        )
            .withLatestFrom(
                connectivityStatus.connectivityStatus(),
                BiFunction { _: Long, state: State -> state }
            )
            .flatMap<Result<LatestRates>> { state ->
                when (state) {
                    State.NOT_CONNECTED -> ratesCache.latestRates
                        .map {
                            when (it) {
                                is Optional.Some -> Result.Ok(it.element)
                                is Optional.None ->
                                    Result.Err(ApiError.networkError(resourceResolver))
                            }.exhaustive
                        }
                        .subscribeOn(schedulerProvider.io)
                    else -> latestRatesApi.latest(baseCurrency)
                        .map {
                            Result.Ok(
                                LatestRates(
                                    baseRate = Rate(
                                        imageUrl = imageUrlCreator.createCountryFlagUrl(it.baseCurrency),
                                        code = it.baseCurrency,
                                        rate = 1f
                                    ),
                                    rates = it.asRatesList()
                                )
                            )
                        }
                        .doOnNext { result -> result.doIfOk(ratesCache::storeLatestRates) }
                        .subscribeOn(schedulerProvider.io)
                }
            }
            .onErrorResumeNext { Flowable.just<Result<LatestRates>>(Result.Err(it)) }
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