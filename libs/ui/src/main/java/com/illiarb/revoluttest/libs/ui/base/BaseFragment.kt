package com.illiarb.revoluttest.libs.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected val onDestroyViewDisposable = CompositeDisposable()

    override fun onDestroyView() {
        onDestroyViewDisposable.clear()
        super.onDestroyView()
    }
}