package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject

class TestConnectivityStatus : ConnectivityStatus {

    private val connectivitySubject = PublishSubject.create<ConnectivityStatus.State>()

    override fun connectivityStatus(): Flowable<ConnectivityStatus.State> =
        connectivitySubject.toFlowable(BackpressureStrategy.LATEST)

    fun accept(state: ConnectivityStatus.State) = connectivitySubject.onNext(state)
}