package com.illiarb.revoluttest.libs.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    private val fragmentLifecycleDisposable = CompositeDisposable()

    protected fun Disposable.unsubscribeOnDestroyView() = fragmentLifecycleDisposable.add(this)

    override fun onDestroyView() {
        fragmentLifecycleDisposable.clear()
        super.onDestroyView()
    }
}