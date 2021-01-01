package com.illiarb.revoluttest.functional.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.recycler.KRecyclerView
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.functional.recyclerview.UiRateItem
import com.illiarb.revoluttest.modules.home.HomeFragment
import com.kaspersky.kaspresso.screens.KScreen
import com.illiarb.revoluttest.libs.ui.R as UiR

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int?
        get() = R.layout.fragment_home

    override val viewClass: Class<*>?
        get() = HomeFragment::class.java

    val appBar = KView { withId(R.id.homeAppBar) }
    val statefulRecyclerView = KView { withId(R.id.homeRatesList) }

    val recyclerView = KRecyclerView(
        { withId(UiR.id.srv_recycler_view) },
        { itemType(::UiRateItem) }
    )

    val loadingAnimationView = KView { withId(UiR.id.srv_animation_view) }
}