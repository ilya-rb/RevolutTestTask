package com.illiarb.revoluttest.modules.home.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.illiarb.revoluttest.databinding.ItemRateBinding
import com.illiarb.revoluttest.modules.home.UiRate
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer

class ItemRateDelegate(
    private val onItemClick: (UiRate) -> Unit,
    private val textChangesConsumer: Consumer<Float>
) : AdapterDelegate<List<UiRate>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemRateViewHolder(
            binding,
            textChangesConsumer,
            onItemClick
        )
    }

    override fun isForViewType(items: List<UiRate>, position: Int): Boolean = true

    override fun onBindViewHolder(
        items: List<UiRate>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder = holder as ItemRateViewHolder

        viewHolder.bind(
            item = items[position],
            payload = if (payloads.isNotEmpty()) {
                payloads.first() as RatesChangedPayload
            } else {
                null
            }
        )
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ItemRateViewHolder).onDetachedFromWindow()
    }

    /**
     * Interface for manipulating the view holder views
     */
    interface ItemRateControls {

        val textChanges: Observable<CharSequence>

        fun setRateValueText(text: String)

        fun requestRateValueFocus(): Boolean

        fun isRateValueFocused(): Boolean

        fun setCaptionText(caption: String)

        fun setBody(body: String)

        fun loadImage(url: String)

        fun setOnItemClickListener(listener: (() -> Unit)?)

        fun setFocusChangeListener(listener: ((Boolean) -> Unit)?)
    }
}