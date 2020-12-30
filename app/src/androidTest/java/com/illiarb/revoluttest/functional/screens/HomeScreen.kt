package com.illiarb.revoluttest.functional.screens

import com.agoda.kakao.recycler.KRecyclerView
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.functional.recyclerview.UiRateItem
import com.illiarb.revoluttest.modules.home.HomeFragment
import com.kaspersky.kaspresso.screens.KScreen

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int?
        get() = R.layout.fragment_home

    override val viewClass: Class<*>?
        get() = HomeFragment::class.java

    val ratesList = KRecyclerView(
        { withId(R.id.homeRatesList) },
        { itemType(::UiRateItem) }
    )
}