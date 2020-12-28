package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.libs.ui.R
import com.illiarb.revoluttest.libs.ui.base.BaseViewModel
import com.illiarb.revoluttest.libs.ui.common.Color
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val ratesService: RatesService
) : BaseViewModel() {

    private val latestRatesDisposable = CompositeDisposable()
    private val ratesListInternal = BehaviorRelay.create<LatestRates>()
    private val rateDisplayFormatter = DecimalFormat(CURRENCY_RATE_FORMAT)

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
            BiFunction<LatestRates, Float, List<UiRate>> { rates, newAmount ->
                listOf(Rate(rates.baseCurrency, newAmount)).plus(rates.rates)
                    .asUiRateList(newAmount, baseCurrency ?: rates.baseCurrency)
                    .sortedByDescending { it.isBaseRate }
            }
        ).subscribe(
            { _ratesList.onNext(it) },
            { /* TODO: Handle error */ Timber.e(it) }
        ).addTo(latestRatesDisposable)
    }

    fun onItemClick(item: UiRate) {
        subscribeToRateUpdates(item.body, item.rate)
    }

    private fun List<Rate>.asUiRateList(baseRate: Float, baseCurrency: String): List<UiRate> {
        return map { rate ->
            val isBase = rate.code == baseCurrency
            UiRate(
                body = rate.code,
                caption = rate.code,
                isBaseRate = isBase,
                rate = if (isBase) {
                    rateDisplayFormatter.format(baseRate)
                } else {
                    rateDisplayFormatter.format(baseRate * rate.rate)
                },
                background = if (isBase) {
                    Color.ResourceColor(R.color.revoluttest_grey_100)
                } else {
                    null
                }
            )
        }
    }

    data class UiRate(
        val body: String,
        val caption: String,
        val rate: String,
        val isBaseRate: Boolean,
        val background: Color?
    )

    companion object {
        const val BASE_CURRENCY_DEFAULT_RATE = 1f
        const val CURRENCY_RATE_FORMAT = "#0.00"
    }
}