package com.illiarb.revoluttest.libs.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val onClearedDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        onClearedDisposable.dispose()
    }

    protected fun Disposable.unsubscribeOnCleared(): Disposable {
        return this.also { onClearedDisposable.add(it) }
    }
}