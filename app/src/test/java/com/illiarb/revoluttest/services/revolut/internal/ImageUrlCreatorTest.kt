package com.illiarb.revoluttest.services.revolut.internal

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.URL
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageUrlCreatorTest {

    private val imageUrlCreator = ImageUrlCreator()

    @ParameterizedTest
    @MethodSource("provideArguments")
    fun `given currency code it should return a url with contains country code of currency`(
        currency: String,
        countryCode: String
    ) {
        val actual = imageUrlCreator.createCountryFlagUrl(currency)
        assertThat(actual).contains(countryCode)
    }

    @ParameterizedTest
    @MethodSource("provideArguments")
    fun `given currency code it should return a valid http url`(
        currency: String,
        countryCode: String
    ) {
        assertDoesNotThrow {
            URL(imageUrlCreator.createCountryFlagUrl(currency))
        }
    }

    companion object {

        @Suppress("unused") // false positive
        @JvmStatic
        fun provideArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("GBP", "gb"),
                Arguments.of("ZAR", "za"),
                Arguments.of("UAH", "ua"),
                Arguments.of("AUD", "au"),
                Arguments.of("BRL", "br")
            )
        }
    }
}