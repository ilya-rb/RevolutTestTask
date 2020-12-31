package com.illiarb.revoluttest.modules.home

import com.google.common.truth.Truth.assertThat
import com.illiarb.revoluttest.modules.home.delegates.RatesChangedPayload
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RatesChangedPayloadTest {

    @ParameterizedTest
    @MethodSource("provideArguments")
    fun `given same old and new ui rate it should return true if image or rate changed respectively`(
        oldRate: UiRate,
        newRate: UiRate,
        expectedPayload: RatesChangedPayload
    ) {
        val actual = RatesChangedPayload.create(oldRate, newRate)
        assertThat(expectedPayload).isEqualTo(actual)
    }

    companion object {

        @Suppress("unused") // false positive
        @JvmStatic
        fun provideArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    createUiRate(imageUrl = "image1"),
                    createUiRate(imageUrl = "image2"),
                    RatesChangedPayload(
                        hasNewImage = true,
                        hasNewRate = false
                    )
                ),
                Arguments.of(
                    createUiRate(imageUrl = "image1"),
                    createUiRate(imageUrl = "image1"),
                    RatesChangedPayload(
                        hasNewImage = false,
                        hasNewRate = false
                    )
                ),
                Arguments.of(
                    createUiRate(rate = "rate1"),
                    createUiRate(rate = "rate2"),
                    RatesChangedPayload(
                        hasNewImage = false,
                        hasNewRate = true
                    )
                ),
                Arguments.of(
                    createUiRate(rate = "rate1"),
                    createUiRate(rate = "rate1"),
                    RatesChangedPayload(
                        hasNewImage = false,
                        hasNewRate = false
                    )
                ),
                Arguments.of(
                    createUiRate(imageUrl = "image1", rate = "rate1"),
                    createUiRate(imageUrl = "image2", rate = "rate2"),
                    RatesChangedPayload(
                        hasNewImage = true,
                        hasNewRate = true
                    )
                )
            )
        }

        private fun createUiRate(
            imageUrl: String = "some_url",
            rate: String = "rate"
        ): UiRate {
            return UiRate(
                imageUrl = imageUrl,
                rate = rate,
                code = "code",
                caption = "caption",
                isBaseRate = false,
                background = null
            )
        }
    }
}