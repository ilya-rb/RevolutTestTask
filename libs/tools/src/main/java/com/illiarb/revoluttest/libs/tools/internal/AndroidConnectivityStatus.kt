package com.illiarb.revoluttest.libs.tools.internal

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus.State
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import javax.inject.Inject

internal class AndroidConnectivityStatus @Inject constructor(
    private val context: Context
) : ConnectivityStatus {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission") //  LIE!
    override fun connectivityStatus(): Flowable<State> {
        return Flowable
            .create<State>({ emitter ->
                val receiver = NetworkChangeBroadcastReceiver(connectivityManager, emitter)
                context.registerReceiver(receiver, IntentFilter(INTENT_ACTION_CONNECTIVITY_CHANGE))
                emitter.setCancellable {
                    context.unregisterReceiver(receiver)
                }
            }, /* mode */ BackpressureStrategy.LATEST)
            .startWithItem(connectivityManager.getCurrentConnectionState())
    }

    private class NetworkChangeBroadcastReceiver(
        private val connectivityManager: ConnectivityManager,
        private val emitter: FlowableEmitter<State>
    ) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (!emitter.isCancelled) {
                emitter.onNext(connectivityManager.getCurrentConnectionState())
            }
        }
    }

    companion object {

        const val INTENT_ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"

        @SuppressLint("MissingPermission")
        private fun ConnectivityManager.getCurrentConnectionState(): State {
            return if (activeNetworkInfo?.isConnectedOrConnecting == true) {
                State.CONNECTED
            } else {
                State.NOT_CONNECTED
            }
        }
    }
}