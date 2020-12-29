package com.illiarb.revoluttest.modules.home

import com.google.common.truth.Truth.assertThat
import com.illiarb.revoluttest.services.revolut.entity.Rate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UiRateMapperTest {

    private val uiRateMapper = UiRateMapper()
    private val ratesList = listOf(
        Rate(imageUrl = "image_url", code = "EUR", rate = 2f),
        Rate(imageUrl = "image_url", code = "GBP", rate = 5f),
        Rate(imageUrl = "image_url", code = "UAH", rate = 5f),
        Rate(imageUrl = "image_url", code = "ZAR", rate = 5f),
        Rate(imageUrl = "image_url", code = "MYR", rate = 5f),
        Rate(imageUrl = "image_url", code = "SGD", rate = 5f),
        Rate(imageUrl = "image_url", code = "IDR", rate = 5f),
        Rate(imageUrl = "image_url", code = "BRL", rate = 5f)
    )

    @Test
    fun `given list of rate with the base rate it should return list of rates multiplied by given base`() {
        val expectedRate = 10.00f
        val actual = uiRateMapper.fromRatesList(
            baseCurrency = "EUR",
            baseRate = 2f,
            rates = ratesList
        )

        assertThat(
            actual.drop(1).all {
                it.rate.toFloat() == expectedRate
            }
        ).isTrue()
    }

    @Test
    fun `given list of rates it should format each down to 2 decimals`() {
        val actual = uiRateMapper.fromRatesList(
            baseCurrency = "EUR",
            baseRate = 2f,
            rates = ratesList.map { it.copy(rate = 5.2354545454545f) }
        )

        assertThat(
            actual.all {
                it.rate.split(".").last().length == 2
            }
        ).isTrue()
    }

    @Test
    fun `given list of rates it should return background color only for the base rate`() {
        val actual = uiRateMapper.fromRatesList(
            baseCurrency = "EUR",
            baseRate = 2f,
            rates = ratesList
        )

        assertThat(actual.first().background).isNotNull()
        assertThat(actual.drop(1).all { it.background == null }).isTrue()
    }
}