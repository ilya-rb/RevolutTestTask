package com.illiarb.revoluttest.libs.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val onClearedDisposable = CompositeDisposable()

    override fun onCleared() {
        onClearedDisposable.clear()
        super.onCleared()
    }
}