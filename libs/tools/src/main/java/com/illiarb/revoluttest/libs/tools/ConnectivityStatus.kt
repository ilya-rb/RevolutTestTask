package com.illiarb.revoluttest.libs.tools

import io.reactivex.rxjava3.core.Flowable

interface ConnectivityStatus {

    fun connectivityStatus(): Flowable<State>

    enum class State {

        CONNECTED,

        NOT_CONNECTED
    }
}