package com.illiarb.revoluttest.functional.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ElevationMatcher(private val predicate: (Float) -> Boolean) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) = Unit

    override fun matchesSafely(item: View?): Boolean = predicate(item?.elevation ?: 0f)
}