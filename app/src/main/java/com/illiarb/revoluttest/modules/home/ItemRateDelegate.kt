package com.illiarb.revoluttest.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.illiarb.revoluttest.databinding.ItemRateBinding
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.libs.ui.image.ImageLoader
import com.illiarb.revoluttest.modules.home.HomeViewModel.UiRate
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import timber.log.Timber

class ItemRateDelegate(
    private val onItemClick: (UiRate) -> Unit,
    private val textChangesConsumer: Consumer<Float>
) : AdapterDelegate<List<UiRate>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, textChangesConsumer, onItemClick)
    }

    override fun isForViewType(items: List<UiRate>, position: Int): Boolean = true

    override fun onBindViewHolder(
        items: List<UiRate>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder = holder as ViewHolder
        viewHolder.bind(
            item = items[position],
            payload = if (payloads.isNotEmpty()) payloads.first() as Bundle else null
        )
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ViewHolder).onDetachedFromWindow()
    }

    private class ViewHolder(
        private val binding: ItemRateBinding,
        private val textChangesConsumer: Consumer<Float>,
        private val onItemClick: (UiRate) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val textChangesDisposable = CompositeDisposable()

        fun onDetachedFromWindow() = textChangesDisposable.clear()

        fun bind(item: UiRate, payload: Bundle?) {
            textChangesDisposable.clear()

            if (payload == null || payload.getBoolean(PAYLOAD_NEW_IMAGE)) {
                ImageLoader.loadImage(binding.itemRateImage, item.imageUrl)
            }

            if (payload == null || payload.getBoolean(PAYLOAD_NEW_RATE)) {
                if (!binding.itemRateValue.isFocused) {
                    binding.itemRateValue.setText(item.rate)
                }
            }

            binding.itemRateCaption.text = item.caption
            binding.itemRateBody.text = item.code

            if (item.isBaseRate) {
                binding.itemRateContainer.setOnClickListener(null)
                binding.itemRateValue.onFocusChangeListener = null

                binding.itemRateValue.textChanges()
                    .map { it?.toString()?.toFloatOrNull() ?: 0f }
                    .subscribe(textChangesConsumer, Consumer { Timber.e(it) })
                    .addTo(textChangesDisposable)
            } else {
                binding.itemRateContainer.setOnClickListener {
                    binding.itemRateValue.requestFocus()
                    onItemClick(item)
                }

                binding.itemRateValue.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        onItemClick(item)
                    }
                }
            }
        }
    }

    companion object {
        const val PAYLOAD_NEW_IMAGE = "image"
        const val PAYLOAD_NEW_RATE = "rate"
    }
}