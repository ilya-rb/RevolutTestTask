package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.libs.ui.base.BaseViewModel
import com.illiarb.revoluttest.libs.ui.common.Text
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.util.Async
import com.illiarb.revoluttest.libs.util.Result
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val ratesService: RatesService,
    private val uiRateMapper: UiRateMapper
) : BaseViewModel() {

    private val latestRatesDisposable = CompositeDisposable()
    private val ratesListInternal = BehaviorRelay.create<LatestRates>()

    private val _ratesList = BehaviorSubject.createDefault<Async<List<UiRate>>>(Async.Loading())
    val ratesList: Observable<Async<List<UiRate>>>
        get() = _ratesList.hide()

    private val _errorMessages = PublishSubject.create<String?>()
    val errorMessages: Observable<String?>
        get() = _errorMessages.hide()

    val emptyViewText: Observable<Text>
        get() = ratesList.map<Text> {
            if (it is Async.Fail) {
                Text.ResourceIdText(R.string.home_empty_view_error)
            } else {
                Text.ResourceIdText(R.string.home_empty_view_loading)
            }
        }

    private val _amountChangedConsumer = BehaviorRelay.create<Float>()
    val amountChangedConsumer: Consumer<Float>
        get() = _amountChangedConsumer

    init {
        // Since API response comes pretty fast
        // make initial subscription with a delay to show initial empty state
        subscribeToRateUpdates(subscriptionDelay = INITIAL_SUBSCRIPTION_DELAY_SECONDS)
    }

    override fun onCleared() {
        super.onCleared()
        latestRatesDisposable.clear()
    }

    private fun subscribeToRateUpdates(
        baseCurrency: String? = null,
        baseRate: String = BASE_CURRENCY_DEFAULT_RATE.toString(),
        subscriptionDelay: Long = 0L
    ) {
        _amountChangedConsumer.accept(baseRate.toFloat())

        latestRatesDisposable.clear()

        ratesService.observeLatestRates(baseCurrency)
            .delaySubscription(subscriptionDelay, TimeUnit.SECONDS)
            .subscribe(
                {
                    when (it) {
                        is Result.Ok -> ratesListInternal.accept(it.data)
                        is Result.Err -> onRateUpdateError(it.error)
                    }.exhaustive
                },
                { onRateUpdateError(it) }
            )
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
            { _ratesList.onNext(Async.Success(it)) },
            { onRateUpdateError(it) }
        ).addTo(latestRatesDisposable)
    }

    fun onItemClick(item: UiRate) {
        subscribeToRateUpdates(item.code, item.rate)
    }

    private fun onRateUpdateError(error: Throwable) {
        Timber.e(error)
        _errorMessages.onNext(error.message)

        if (_ratesList.value !is Async.Success) {
            _ratesList.onNext(Async.Fail(error))
        }
    }

    companion object {
        const val BASE_CURRENCY_DEFAULT_RATE = 1f
        const val INITIAL_SUBSCRIPTION_DELAY_SECONDS = 2L
    }
}