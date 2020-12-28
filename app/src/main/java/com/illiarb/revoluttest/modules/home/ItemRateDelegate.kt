package com.illiarb.revoluttest.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.illiarb.revoluttest.databinding.ItemRateBinding
import com.illiarb.revoluttest.libs.ui.ext.addTo
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
            items[position],
            null//if (payloads.isNotEmpty()) payloads.first() as Bundle else null
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

        fun bind(item: UiRate, bundle: Bundle?) {
            binding.itemRateCaption.text = item.caption
            binding.itemRateValue.isEnabled = item.isBaseRate
            binding.itemRateBody.text = item.body

            textChangesDisposable.clear()

            if (!binding.itemRateValue.hasFocus()) {
                binding.itemRateValue.setText(item.rate)
            }

            if (item.isBaseRate) {
                if (!binding.itemRateValue.isFocused) {
                    binding.itemRateValue.requestFocus()
                }
            } else {
                binding.itemRateValue.clearFocus()
            }

            binding.itemRateValue.textChanges()
                .skipInitialValue()
                .map { it?.toString()?.toFloatOrNull() ?: 0f }
                .subscribe(textChangesConsumer, Consumer { Timber.e(it) })
                .addTo(textChangesDisposable)

            if (item.isBaseRate) {
                binding.itemRateContainer.setOnClickListener(null)
            } else {
                binding.itemRateContainer.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }
}