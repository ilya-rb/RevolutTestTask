package com.illiarb.revoluttest.modules.main

import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.ui.base.BaseViewModel
import com.illiarb.revoluttest.libs.ui.common.Text
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    connectivityStatus: ConnectivityStatus
) : BaseViewModel() {

    private val _connectionState = BehaviorSubject.create<ConnectionState>()
    val connectionState: Observable<ConnectionState>
        get() = _connectionState

    init {
        connectivityStatus.connectivityStatus()
            .subscribe(
                { _connectionState.onNext(it.asConnectionState()) },
                { /* TODO: Log error */ Timber.e(it) }
            )
            .unsubscribeOnCleared()
    }

    private fun ConnectivityStatus.State.asConnectionState(): ConnectionState {
        return ConnectionState(
            showLabel = this == ConnectivityStatus.State.NOT_CONNECTED,
            notConnectedText = Text.ResourceString(R.string.main_network_not_connected)
        )
    }

    data class ConnectionState(
        val showLabel: Boolean,
        val notConnectedText: Text
    )
}