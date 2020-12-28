package com.illiarb.revoluttest.libs.ui.ext

import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.updatePadding
import com.illiarb.revoluttest.libs.ui.common.Padding

// Adapted from here:
// https://medium.com/androiddevelopers/windowinsets-listeners-to-layouts-8f9ccc8fa4d1

fun View.doOnApplyWindowInsets(f: (View, WindowInsets, Padding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun View.addStatusBarTopPadding() {
    doOnApplyWindowInsets { view, windowInsets, padding ->
        val statusBarTopInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            windowInsets.getInsets(WindowInsets.Type.statusBars()).top
        } else {
            windowInsets.systemWindowInsetTop
        }
        view.updatePadding(top = padding.top + statusBarTopInset)
    }
}

fun View.addNavigationBarBottomPadding() {
    doOnApplyWindowInsets { view, windowInsets, padding ->
        val statusBarBottomInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            windowInsets.getInsets(WindowInsets.Type.statusBars()).bottom
        } else {
            windowInsets.systemWindowInsetBottom
        }
        view.updatePadding(bottom = padding.bottom + statusBarBottomInset)
    }
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View) = Unit
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }
        })
    }
}

private fun recordInitialPaddingForView(view: View) =
    Padding(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom
    )