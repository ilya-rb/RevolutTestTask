package com.illiarb.revoluttest.libs.ui.toolbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.illiarb.revoluttest.libs.ui.R
import com.illiarb.revoluttest.libs.ui.ext.setVisible

class FragmentToolbarLifecycleCallbacks(
    private val toolbar: Toolbar
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)

        if (f is HasToolbar) {
            configureToolbarWithOptions(f.getToolbarOptions())
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)

        if (f is HasToolbar) {
            resetToolbar()
        }
    }

    private fun resetToolbar() {
        toolbar.setVisible(false)
        toolbar.title = null
        toolbar.navigationIcon = null
        toolbar.setNavigationOnClickListener(null)
    }

    private fun configureToolbarWithOptions(options: HasToolbar.ToolbarOptions) {
        toolbar.setVisible(options.isVisible)
        toolbar.title = options.title

        toolbar.setNavigationOnClickListener { options.onNavigationButtonClick() }
        toolbar.navigationIcon = if (options.hasCloseButton) {
            ContextCompat.getDrawable(toolbar.context, R.drawable.ic_close)
        } else {
            null
        }
    }
}