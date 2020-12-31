package com.illiarb.revoluttest.modules.home

import com.google.common.truth.Truth.assertThat
import com.illiarb.revoluttest.common.TestConnectivityStatus
import com.illiarb.revoluttest.common.TestLatestRatesApi
import com.illiarb.revoluttest.common.TestRatesCache
import com.illiarb.revoluttest.common.TestResourceResolver
import com.illiarb.revoluttest.common.TestSchedulerProvider
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import com.illiarb.revoluttest.modules.home.HomeViewModel.Companion.INITIAL_SUBSCRIPTION_DELAY_SECONDS
import com.illiarb.revoluttest.services.revolut.internal.ImageUrlCreator
import com.illiarb.revoluttest.services.revolut.internal.RevolutRatesService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.roundToInt
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class HomeViewModelTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val testConnectivityStatus = TestConnectivityStatus()

    private val viewModel = HomeViewModel(
        RevolutRatesService(
            TestLatestRatesApi(),
            ImageUrlCreator(),
            testSchedulerProvider,
            TestRatesCache(),
            testConnectivityStatus,
            TestResourceResolver()
        ),
        UiRateMapper(),
        testSchedulerProvider
    )

    @BeforeEach
    fun beforeEach() {
        testConnectivityStatus.setStartWithOnSubscribe(State.CONNECTED)
        testSchedulerProvider.advanceTimeBy(INITIAL_SUBSCRIPTION_DELAY_SECONDS)
    }

    @Test
    fun `it should return a list with rates with only first item as a base rate`() {
        val rates = viewModel.ratesList.test()

        repeat(times = 10) { _ ->
            testSchedulerProvider.advanceToNextRateUpdate()

            rates.values().drop(1).forEach { state ->
                assertThat(state.data().first().isBaseRate).isTrue()
                assertThat(state.data().drop(1).none { it.isBaseRate }).isTrue()
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
            // Drop 1 to skip Async.Loading
            val actual = rates.values().drop(1)[i + 1].data().first().rate.toFloat().roundToInt()

            assertThat(expected).isEqualTo(actual)
        }
    }

    @Test
    fun `when new rate is selected it should become a new base rate`() {
        val rates = viewModel.ratesList.test()


        // Init with the first rates
        testSchedulerProvider.advanceToNextRateUpdate()

        val itemsSize = rates.values()[1].data().size
        val random = Random(100)
        val selectedNewRateCode = rates.values()
            // Drop 1 to skip Async.Loading
            .drop(1)
            .first().data()[random.nextInt(1, itemsSize)].code

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

        val newBase = rates.values().last().data().first()

        assertThat(newBase.code).isEqualTo(selectedNewRateCode)
        assertThat(newBase.isBaseRate).isTrue()
    }
}