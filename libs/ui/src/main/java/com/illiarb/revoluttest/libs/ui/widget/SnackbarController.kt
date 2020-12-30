package com.illiarb.revoluttest.libs.ui.widget

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class SnackbarController {

    private var snackbar: Snackbar? = null

    fun showOrUpdateMessage(message: String, view: View, duration: Int) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, message, duration)
                .apply {
                    animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            snackbar = null
                        }
                    })
                }
                .also { it.show() }
        } else {
            snackbar!!.setText(message)
        }
    }
}