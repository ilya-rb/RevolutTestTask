package com.illiarb.revoluttest.services.revolut.internal

import com.illiarb.revoluttest.common.TestConnectivityStatus
import com.illiarb.revoluttest.common.TestLatestRatesApi
import com.illiarb.revoluttest.common.TestRatesCache
import com.illiarb.revoluttest.common.TestResourceResolver
import com.illiarb.revoluttest.common.TestSchedulerProvider
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.util.Result
import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import com.illiarb.revoluttest.services.revolut.internal.cache.RatesCache
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevolutRatesServiceTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val testConnectivityStatus = TestConnectivityStatus()

    @BeforeEach
    fun beforeEach() {
        testConnectivityStatus.setStartWithOnSubscribe(ConnectivityStatus.State.CONNECTED)
    }

    @Test
    fun `given null as base currency it should return default currency as EUR`() {
        val revolutRatesService = createRatesService()
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
        val revolutRatesService = createRatesService()
        val observer = revolutRatesService.observeLatestRates(baseCurrency = "GBP").test()

        repeat(times = 10) { i ->
            observer.assertValueCount(i)
            testSchedulerProvider.advanceToNextRateUpdate()
        }
    }

    @Test
    fun `when lost connection it should switch to cached response`() {
        val mockedCache = mockk<RatesCache>()
        val revolutRatesService = createRatesService(cache = mockedCache)

        revolutRatesService.observeLatestRates(baseCurrency = "GBP").test()

        testConnectivityStatus.accept(ConnectivityStatus.State.NOT_CONNECTED)
        testSchedulerProvider.advanceToNextRateUpdate()

        verify(exactly = 1) {
            mockedCache.latestRates
        }
    }

    @Test
    fun `when connection is back it should switch back to api calls`() {
        val mockedApi = mockk<LatestRatesApi>()
        val revolutRatesService = createRatesService(api = mockedApi)

        testConnectivityStatus.accept(ConnectivityStatus.State.NOT_CONNECTED)

        revolutRatesService.observeLatestRates(baseCurrency = "GBP").test()

        verify(exactly = 0) {
            mockedApi.latest(any())
        }

        testConnectivityStatus.accept(ConnectivityStatus.State.CONNECTED)
        testSchedulerProvider.advanceToNextRateUpdate()

        verify(exactly = 1) {
            mockedApi.latest(any())
        }
    }

    private fun createRatesService(
        cache: RatesCache = TestRatesCache(),
        api: LatestRatesApi = TestLatestRatesApi()
    ): RatesService {
        return RevolutRatesService(
            api,
            ImageUrlCreator(),
            testSchedulerProvider,
            cache,
            testConnectivityStatus,
            TestResourceResolver()
        )
    }
}