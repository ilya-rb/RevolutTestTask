package com.illiarb.revoluttest

import androidx.test.platform.app.InstrumentationRegistry
import com.illiarb.revoluttest.services.revolut.RatesService.LatestRates
import com.illiarb.revoluttest.services.revolut.entity.Rate
import com.illiarb.revoluttest.services.revolut.internal.cache.BinaryPrefsRatesCache
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RatesCacheTest {

    private lateinit var ratesCache: BinaryPrefsRatesCache

    private val latestRates = LatestRates(
        baseRate = Rate("url", "EUR", 1f),
        rates = listOf(
            Rate("url", "GBP", 0.75f),
            Rate("url", "ZAR", 2.25f),
            Rate("url", "UAH", 3f),
        )
    )

    @BeforeEach
    fun beforeEach() {
        with(InstrumentationRegistry.getInstrumentation()) {
            // Preferences should be initialized on the main thread
            runOnMainSync {
                ratesCache = BinaryPrefsRatesCache(context)
            }
        }
    }

    @AfterEach
    fun afterEach() {
        ratesCache.clearCache()
    }

    @Test
    @DisplayName("Given latest rates it should write and read it successfully")
    fun readWriteTest() {
        val ratesObservable = ratesCache.latestRates.test()

        ratesCache.storeLatestRates(latestRates)

        ratesObservable
            .assertNoErrors()
            .assertValue { it == latestRates }
    }

    @Test
    @DisplayName("Given latest rates in cache it should clear it")
    fun clearCacheTest() {
        ratesCache.storeLatestRates(latestRates)

        val ratesObservable = ratesCache.latestRates.test()
        ratesCache.clearCache()

        ratesObservable
            .assertNoErrors()
            .assertValue { it.rates.isEmpty() }
    }

    @Test
    @DisplayName("Given latest rates it should emit an item from cache to observer")
    fun observeAfterWriteTest() {
        val ratesObservable = ratesCache.latestRates.test()

        ratesCache.storeLatestRates(latestRates)

        ratesObservable
            .assertNoErrors()
            .assertValueCount(1)
    }
}