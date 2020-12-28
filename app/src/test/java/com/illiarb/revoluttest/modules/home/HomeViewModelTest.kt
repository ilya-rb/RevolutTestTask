package com.illiarb.revoluttest.modules.home

import com.google.common.truth.Truth.assertThat
import com.illiarb.revoluttest.common.TestLatestRatesApi
import com.illiarb.revoluttest.common.TestSchedulerProvider
import com.illiarb.revoluttest.services.revolut.internal.ImageUrlCreator
import com.illiarb.revoluttest.services.revolut.internal.RevolutRatesService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.roundToInt
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class HomeViewModelTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val viewModel = HomeViewModel(
        RevolutRatesService(
            TestLatestRatesApi(),
            ImageUrlCreator(),
            testSchedulerProvider
        ),
        UiRateMapper()
    )

    @Test
    fun `it should return a list with rates with only first item as a base rate`() {
        val rates = viewModel.ratesList.test()

        repeat(times = 10) { i ->
            testSchedulerProvider.advanceToNextRateUpdate()
            rates.assertValueAt(i) {
                it.first().isBaseRate
                it.drop(1).all { rate -> !rate.isBaseRate }
            }
        }
    }

    @Test
    fun `given entered amount it should return first item with a entered rate`() {
        val rates = viewModel.ratesList.test()

        // Init with the first rates
        testSchedulerProvider.advanceToNextRateUpdate()

        val inputBuilder = StringBuilder()
        // emulate typing
        repeat(times = 3) { i ->
            viewModel.amountChangedConsumer.accept(inputBuilder.append("1").toString().toFloat())

            val expected = inputBuilder.toString().toInt()
            val actual = rates.values()[i + 1].first().rate.toFloat().roundToInt()
            assertThat(expected).isEqualTo(actual)
        }
    }

    @Test
    fun `when new rate is selected it should become a new base rate`() {
        val rates = viewModel.ratesList.test()

        // Init with the first rates
        testSchedulerProvider.advanceToNextRateUpdate()

        val itemsSize = rates.values().first().size
        val random = Random(100)
        val selectedNewRateCode = rates.values().first()[random.nextInt(1, itemsSize)].code

        viewModel.onItemClick(
            UiRate(
                imageUrl = "url",
                // Select new item from the list
                code = selectedNewRateCode,
                caption = "caption",
                rate = "100",
                isBaseRate = false,
                background = null
            )
        )

        testSchedulerProvider.advanceToNextRateUpdate()

        val newBase = rates.values().last().first()
        assertThat(newBase.code).isEqualTo(selectedNewRateCode)
        assertThat(newBase.isBaseRate).isTrue()
    }
}