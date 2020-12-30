package com.illiarb.revoluttest.services.revolut.internal.cache

import android.content.Context
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BinaryPrefsRatesCache @Inject constructor(context: Context) : RatesCache {

    private val ratesSubject = PublishSubject.create<LatestRates>()
    private val store = BinaryPreferencesBuilder(context)
        .name(STORE_NAME)
        .externalStorage(false)
        .registerPersistable(KEY_LATEST_RATES, LatestRatestPersistable::class.java)
        .exceptionHandler { Timber.e(it) }
        .build()

    override val latestRates: Flowable<LatestRates>
        get() = ratesSubject.toFlowable(/* strategy */ BackpressureStrategy.LATEST)

    override fun storeLatestRates(rates: LatestRates) {
        store.edit().putPersistable(
            KEY_LATEST_RATES,
            LatestRatestPersistable(
                baseRate = RatePersistable(
                    rates.baseRate.imageUrl,
                    rates.baseRate.code,
                    rates.baseRate.rate
                ),
                rates = rates.rates.map { rate ->
                    RatePersistable(
                        imageUrl = rate.imageUrl,
                        code = rate.code,
                        rate = rate.rate
                    )
                }.toMutableList()
            )
        ).apply()

        ratesSubject.onNext(rates)
    }

    companion object {
        private const val STORE_NAME = "rates_cache"
        private const val KEY_LATEST_RATES = "latest_rates"
    }
}