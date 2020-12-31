package com.illiarb.revoluttest.modules.home.delegates

import androidx.recyclerview.widget.RecyclerView
import com.illiarb.revoluttest.databinding.ItemRateBinding
import com.illiarb.revoluttest.libs.ui.image.ImageLoader
import com.illiarb.revoluttest.modules.home.UiRate
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer

class ItemRateViewHolder(
    private val binding: ItemRateBinding,
    textChangesConsumer: Consumer<Float>,
    onItemClick: (UiRate) -> Unit
) : RecyclerView.ViewHolder(binding.root), ItemRateDelegate.ItemRateControls {

    private val textChangesDisposable = CompositeDisposable()
    private val itemRateController = ItemRateController(
        itemRateControls = this,
        lifecycleDisposable = textChangesDisposable,
        textChangesConsumer = textChangesConsumer,
        onItemClick = onItemClick
    )

    fun onDetachedFromWindow() = textChangesDisposable.clear()

    fun bind(item: UiRate, payload: RatesChangedPayload?) {
        textChangesDisposable.clear()
        itemRateController.bind(item, payload)
    }

    override val textChanges: Observable<CharSequence>
        get() = binding.itemRateValue.textChanges()

    override fun loadImage(url: String) = ImageLoader.loadImage(binding.itemRateImage, url)
    override fun requestRateValueFocus() = binding.itemRateValue.requestFocus()
    override fun isRateValueFocused(): Boolean = binding.itemRateValue.isFocused

    override fun setRateValueText(text: String) {
        binding.itemRateValue.setText(text)
    }

    override fun setCaptionText(caption: String) {
        binding.itemRateCaption.text = caption
    }

    override fun setBody(body: String) {
        binding.itemRateBody.text = body
    }

    override fun setFocusChangeListener(listener: ((Boolean) -> Unit)?) {
        if (listener == null) {
            binding.itemRateValue.onFocusChangeListener = null
        } else {
            binding.itemRateValue.setOnFocusChangeListener { _, hasFocus ->
                listener(hasFocus)
            }
        }
    }

    override fun setOnItemClickListener(listener: (() -> Unit)?) {
        if (listener == null) {
            binding.itemRateContainer.setOnClickListener(null)
        } else {
            binding.itemRateContainer.setOnClickListener {
                listener()
            }
        }
    }
}