package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class TestConnectivityStatus : ConnectivityStatus {

    private val connectivitySubject = BehaviorSubject.create<ConnectivityStatus.State>()
    private var startWithOnSubscribe: ConnectivityStatus.State = ConnectivityStatus.State.CONNECTED

    override fun connectivityStatus(): Flowable<ConnectivityStatus.State> =
        connectivitySubject.toFlowable(BackpressureStrategy.LATEST)
            .startWithItem(startWithOnSubscribe)

    fun accept(state: ConnectivityStatus.State) = connectivitySubject.onNext(state)

    fun setStartWithOnSubscribe(state: ConnectivityStatus.State) {
        startWithOnSubscribe = state
    }
}