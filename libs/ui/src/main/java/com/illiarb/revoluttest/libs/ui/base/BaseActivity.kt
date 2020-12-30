package com.illiarb.revoluttest.libs.ui.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    protected val onStopDisposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()

        if (!isChangingConfigurations) {
            onStopDisposable.dispose()
        }
    }
}