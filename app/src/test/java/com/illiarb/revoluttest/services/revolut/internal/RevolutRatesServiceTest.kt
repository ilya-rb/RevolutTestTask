package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.common.TestLatestRatesApi
import com.illiarb.revoluttest.common.TestSchedulerProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevolutRatesServiceTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val revolutRatesService =
        RevolutRatesService(TestLatestRatesApi(), ImageUrlCreator(), testSchedulerProvider)

    @Test
    fun `given null as base currency it should return default currency as EUR`() {
        val observer = revolutRatesService.observeLatestRates(baseCurrency = null).test()

        repeat(times = 10) { i ->
            testSchedulerProvider.testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
            observer.assertValueAt(i) {
                it.baseRate.code == TestLatestRatesApi.TEST_BASE_CURRENCY
            }
        }
    }

    @Test
    fun `given any currency it should return a new value every second`() {
        val observer = revolutRatesService.observeLatestRates(baseCurrency = "GBP").test()

        repeat(times = 10) { i ->
            observer.assertValueCount(i)
            testSchedulerProvider.testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        }
    }
}