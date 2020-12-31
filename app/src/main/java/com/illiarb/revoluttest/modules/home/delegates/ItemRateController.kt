package com.illiarb.revoluttest.modules.home.delegates

import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.modules.home.UiRate
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import timber.log.Timber

class ItemRateController(
    private val itemRateControls: ItemRateDelegate.ItemRateControls,
    private val lifecycleDisposable: CompositeDisposable,
    private val textChangesConsumer: Consumer<Float>,
    private val onItemClick: (UiRate) -> Unit
) {

    fun bind(item: UiRate, payload: RatesChangedPayload?) {
        if (payload == null || payload.hasNewImage) {
            itemRateControls.loadImage(item.imageUrl)
        }

        if (payload == null || payload.hasNewRate) {
            if (!itemRateControls.isRateValueFocused()) {
                itemRateControls.setRateValueText(item.rate)
            }
        }

        itemRateControls.setCaptionText(item.caption)
        itemRateControls.setBody(item.code)
        itemRateControls.setOnEditorActionDoneListener {
            itemRateControls.hideKeyboard()
        }

        if (item.isBaseRate) {
            itemRateControls.setOnItemClickListener(null)
            itemRateControls.setFocusChangeListener(null)
            itemRateControls.requestRateValueFocus()

            itemRateControls.textChanges
                .map { it?.toString()?.toFloatOrNull() ?: 0f }
                .subscribe(textChangesConsumer, Consumer { Timber.e(it) })
                .addTo(lifecycleDisposable)
        } else {
            itemRateControls.setOnItemClickListener {
                itemRateControls.requestRateValueFocus()
                onItemClick(item)
            }

            itemRateControls.setFocusChangeListener { hasFocus ->
                if (hasFocus) {
                    onItemClick(item)
                }
            }
        }
    }
}