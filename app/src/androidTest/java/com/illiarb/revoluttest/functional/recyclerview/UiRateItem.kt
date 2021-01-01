package com.illiarb.revoluttest.functional.recyclerview

import android.view.View
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.text.KTextView
import com.illiarb.revoluttest.R
import org.hamcrest.Matcher

class UiRateItem(parent: Matcher<View>) : KRecyclerItem<UiRateItem>(parent) {

    inline fun rateCodeWithText(text: String, block: (KTextView) -> Unit) {
        block(
            KTextView {
                withId(R.id.item_rate_body)
                withText(text)
            }
        )
    }

    inline fun rateValueEditText(atPosition: Int, block: (KEditText) -> Unit) {
        block(
            KEditText {
                withIndex(atPosition) {
                    withId(R.id.item_rate_value)
                }
            }
        )
    }
}