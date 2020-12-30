package com.illiarb.revoluttest.libs.ui.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val onStopDisposable = CompositeDisposable()

    override fun onStop() {
        onStopDisposable.clear()
        super.onStop()
    }
}