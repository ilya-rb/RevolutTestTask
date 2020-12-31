package com.illiarb.revoluttest.modules.home

import com.illiarb.revoluttest.modules.home.delegates.ItemRateController
import com.illiarb.revoluttest.modules.home.delegates.ItemRateDelegate.ItemRateControls
import com.jakewharton.rxrelay3.PublishRelay
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ItemRateControllerTest {

    private val disposable = CompositeDisposable()
    private val textChangedConsumer = PublishRelay.create<Float>()
    private val itemRateControls = mockk<ItemRateControls>(relaxed = true)
    private val itemRateController = ItemRateController(
        itemRateControls = itemRateControls,
        lifecycleDisposable = disposable,
        textChangesConsumer = textChangedConsumer,
        onItemClick = {}
    )

    @Test
    fun `given ui rate it should not load image if its not changed`() {
        val uiRate = createDefaultUiRate()

        itemRateController.bind(
            uiRate,
            payload = RatesChangedPayload(hasNewImage = false)
        )

        verify(exactly = 0) {
            itemRateControls.loadImage(any())
        }
    }

    @Test
    fun `given base ui rate it shouldn't change text if edit text is focused`() {
        val uiRate = createDefaultUiRate()

        every { itemRateControls.isRateValueFocused() }.returns(true)

        itemRateController.bind(
            uiRate,
            payload = RatesChangedPayload(hasNewRate = true)
        )

        verify(exactly = 0) {
            itemRateControls.setRateValueText(any())
        }
    }

    @Test
    fun `given base ui rate it should clear all listeners`() {
        val uiRate = createDefaultUiRate(isBase = true)

        itemRateController.bind(uiRate, payload = null)

        verify(exactly = 1) { itemRateControls.setOnItemClickListener(null) }
        verify(exactly = 1) { itemRateControls.setFocusChangeListener(null) }
    }

    private fun createDefaultUiRate(isBase: Boolean = false) = UiRate(
        imageUrl = "url",
        code = "code",
        caption = "caption",
        isBaseRate = isBase,
        background = null,
        rate = "rate"
    )
}