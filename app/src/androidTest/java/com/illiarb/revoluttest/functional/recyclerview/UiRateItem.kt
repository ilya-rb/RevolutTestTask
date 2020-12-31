package com.illiarb.revoluttest.functional.recyclerview

import android.view.View
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.recycler.KRecyclerItem
import com.illiarb.revoluttest.R
import org.hamcrest.Matcher

class UiRateItem(parent: Matcher<View>) : KRecyclerItem<UiRateItem>(parent) {

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