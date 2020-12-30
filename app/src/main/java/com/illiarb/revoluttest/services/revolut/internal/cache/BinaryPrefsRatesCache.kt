package com.illiarb.revoluttest.services.revolut.internal.cache

import android.content.Context
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.illiarb.revoluttest.services.revolut.entity.Rate
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
                baseRate = rates.baseRate.asPersistableRate(),
                rates = rates.rates.map { it.asPersistableRate() }.toMutableList()
            )
        ).commit()

        ratesSubject.onNext(readCachedRates())
    }

    override fun clearCache() {
        store.edit().clear().commit()
        ratesSubject.onNext(readCachedRates())
    }

    private fun readCachedRates(): LatestRates {
        val cached = store.getPersistable(KEY_LATEST_RATES, LatestRatestPersistable())

        return LatestRates(
            baseRate = cached.baseRate.asDomainRate(),
            rates = cached.rates.map {
                it.asDomainRate()
            }
        )
    }

    private fun RatePersistable.asDomainRate(): Rate =
        Rate(
            imageUrl = imageUrl,
            code = code,
            rate = rate
        )

    private fun Rate.asPersistableRate(): RatePersistable =
        RatePersistable(
            imageUrl = imageUrl,
            code = code,
            rate = rate
        )

    companion object {
        private const val STORE_NAME = "rates_cache"
        private const val KEY_LATEST_RATES = "latest_rates"
    }
}