package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.common.TestConnectivityStatus
import com.illiarb.revoluttest.common.TestLatestRatesApi
import com.illiarb.revoluttest.common.TestRatesCache
import com.illiarb.revoluttest.common.TestResourceResolver
import com.illiarb.revoluttest.common.TestSchedulerProvider
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.util.Result
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevolutRatesServiceTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val testConnectivityStatus = TestConnectivityStatus()

    private val revolutRatesService = RevolutRatesService(
        TestLatestRatesApi(),
        ImageUrlCreator(),
        testSchedulerProvider,
        TestRatesCache(),
        testConnectivityStatus,
        TestResourceResolver()
    )

    @BeforeEach
    fun beforeEach() {
        testConnectivityStatus.setStartWithOnSubscribe(ConnectivityStatus.State.CONNECTED)
    }

    @Test
    fun `given null as base currency it should return default currency as EUR`() {
        val observer = revolutRatesService.observeLatestRates(baseCurrency = null).test()

        repeat(times = 10) { i ->
            testSchedulerProvider.advanceToNextRateUpdate()
            observer.assertValueAt(i) {
                it is Result.Ok
                it.unwrap().baseRate.code == TestLatestRatesApi.TEST_BASE_CURRENCY
            }
        }
    }

    @Test
    fun `given any currency it should return a new value every second`() {
        val observer = revolutRatesService.observeLatestRates(baseCurrency = "GBP").test()

        repeat(times = 10) { i ->
            observer.assertValueCount(i)
            testSchedulerProvider.advanceToNextRateUpdate()
        }
    }
}