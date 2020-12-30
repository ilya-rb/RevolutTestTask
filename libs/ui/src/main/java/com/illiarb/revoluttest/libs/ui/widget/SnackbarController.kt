package com.illiarb.revoluttest.libs.ui.widget

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.illiarb.revoluttest.libs.ui.R
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.ui.ext.getColorAttr
import com.google.android.material.R as MaterialR

class SnackbarController {

    private var snackbar: Snackbar? = null

    fun showOrUpdateMessage(
        message: String,
        view: View,
        duration: Int,
        style: Style = Style.REGULAR,
        init: (Snackbar) -> Unit = {}
    ) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, message, duration)
                .apply {
                    animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            snackbar = null
                        }
                    })

                    when (style) {
                        Style.REGULAR -> {
                            setBackgroundTint(view.context.getColorAttr(MaterialR.attr.colorSurface))
                            setTextColor(view.context.getColorAttr(MaterialR.attr.colorOnSurface))
                        }
                        Style.ERROR -> {
                            setBackgroundTint(view.context.getColorAttr(MaterialR.attr.colorError))
                            setTextColor(view.context.getColorAttr(MaterialR.attr.colorOnError))
                        }
                        Style.SUCCESS -> {
                            setBackgroundTint(view.context.getColorAttr(R.attr.colorSuccess))
                            setTextColor(view.context.getColorAttr(R.attr.colorOnSuccess))
                        }
                    }.exhaustive

                    init(this)
                }
                .also { it.show() }
        } else {
            snackbar!!.setText(message).apply {
                init(this)
            }
        }
    }

    fun dismiss() {
        snackbar?.dismiss()
        snackbar = null
    }

    enum class Style {
        REGULAR,
        ERROR,
        SUCCESS
    }
}