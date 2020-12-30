package com.illiarb.revoluttest.modules.main

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import com.illiarb.revoluttest.libs.ui.base.BaseViewModel
import com.illiarb.revoluttest.libs.ui.ext.addTo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    connectivityStatus: ConnectivityStatus
) : BaseViewModel() {

    private val _connectionState = BehaviorSubject.create<State>()
    val connectionState: Observable<State>
        get() = _connectionState

    init {
        connectivityStatus.connectivityStatus()
            .subscribe(_connectionState::onNext) { Timber.e(it) }
            .addTo(onClearedDisposable)
    }
}