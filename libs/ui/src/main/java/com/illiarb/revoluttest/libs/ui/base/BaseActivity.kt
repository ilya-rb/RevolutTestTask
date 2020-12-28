package com.illiarb.revoluttest.libs.ui.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    private val activityLifecycleDisposable = CompositeDisposable()

    protected fun Disposable.unsubscribeOnStop() = activityLifecycleDisposable.add(this)

    override fun onStop() {
        super.onStop()

        if (!isChangingConfigurations) {
            activityLifecycleDisposable.dispose()
        }
    }
}