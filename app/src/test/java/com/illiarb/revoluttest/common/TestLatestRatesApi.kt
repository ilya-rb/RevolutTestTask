package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesResponse
import io.reactivex.rxjava3.core.Flowable
import kotlin.random.Random

internal class TestLatestRatesApi : LatestRatesApi {

    private val random = Random(10)
    private val testCurrencies = listOf("GBP", "ZAR", "UAH", "USD", "BRL", "AUD", "IDR")

    override fun latest(baseCurrency: String?): Flowable<LatestRatesResponse> {
        return Flowable.fromCallable {
            LatestRatesResponse(
                baseCurrency = baseCurrency ?: TEST_BASE_CURRENCY,
                rates = testCurrencies.associateWith {
                    0.5f * random.nextInt()
                }
            )
        }
    }

    companion object {
        const val TEST_BASE_CURRENCY = "EUR"
    }
}