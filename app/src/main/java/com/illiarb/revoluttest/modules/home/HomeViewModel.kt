package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.libs.ui.base.BaseViewModel
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val ratesService: RatesService,
    private val uiRateMapper: UiRateMapper
) : BaseViewModel() {

    private val latestRatesDisposable = CompositeDisposable()
    private val ratesListInternal = BehaviorRelay.create<LatestRates>()

    private val _ratesList = PublishSubject.create<List<UiRate>>()
    val ratesList: Observable<List<UiRate>>
        get() = _ratesList.hide()

    private val _amountChangedConsumer = BehaviorRelay.create<Float>()
    val amountChangedConsumer: Consumer<Float>
        get() = _amountChangedConsumer

    init {
        subscribeToRateUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        latestRatesDisposable.clear()
    }

    private fun subscribeToRateUpdates(
        baseCurrency: String? = null,
        baseRate: String = BASE_CURRENCY_DEFAULT_RATE.toString()
    ) {
        _amountChangedConsumer.accept(baseRate.toFloat())

        latestRatesDisposable.clear()

        ratesService.observeLatestRates(baseCurrency)
            .subscribe(ratesListInternal::accept) { Timber.e(it) }
            .addTo(latestRatesDisposable)

        Flowable.combineLatest(
            ratesListInternal.toFlowable(/* strategy */ BackpressureStrategy.LATEST),
            _amountChangedConsumer.toFlowable(/* strategy */ BackpressureStrategy.LATEST),
            { rates, newAmount ->
                val ratesWithBase = listOf(
                    Rate(
                        imageUrl = rates.baseRate.imageUrl,
                        code = rates.baseRate.code,
                        rate = newAmount
                    )
                ).plus(rates.rates)

                uiRateMapper.fromRatesList(
                    rates = ratesWithBase,
                    baseCurrency = baseCurrency ?: rates.baseRate.code,
                    baseRate = newAmount
                ).sortedByDescending { it.isBaseRate }
            }
        ).subscribe(
            { _ratesList.onNext(it) },
            { /* TODO: Handle error */ Timber.e(it) }
        ).addTo(latestRatesDisposable)
    }

    fun onItemClick(item: UiRate) {
        subscribeToRateUpdates(item.code, item.rate)
    }

    companion object {
        const val BASE_CURRENCY_DEFAULT_RATE = 1f
    }
}