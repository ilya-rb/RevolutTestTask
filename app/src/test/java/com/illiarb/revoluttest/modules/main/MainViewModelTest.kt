package com.illiarb.revoluttest.modules.main

import com.illiarb.revoluttest.common.TestConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MainViewModelTest {

    private val connectivityStatus = TestConnectivityStatus()
    private val mainViewModel = MainViewModel(connectivityStatus)

    @ParameterizedTest
    @MethodSource("provideArguments")
    fun `given connection state it should return respective show label state`(
        newState: State,
        showLabel: Boolean
    ) {
        val observer = mainViewModel.connectionState.test()
        connectivityStatus.accept(newState)

        observer.assertValue { it.showLabel == showLabel }
    }

    companion object {

        @Suppress("unused") // false positive
        @JvmStatic
        fun provideArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(State.CONNECTED, false),
                Arguments.of(State.NOT_CONNECTED, true)
            )
        }
    }
}