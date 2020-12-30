package com.illiarb.revoluttest.libs.tools.internal

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

internal class AndroidConnectivityStatus @Inject constructor(
    context: Context
) : ConnectivityStatus {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission") //  I DO have a permission!
    override fun connectivityStatus(): Flowable<ConnectivityStatus.State> {
        return Flowable.create<ConnectivityStatus.State>({ emitter ->
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    if (!emitter.isCancelled) {
                        emitter.onNext(ConnectivityStatus.State.CONNECTED)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)

                    if (!emitter.isCancelled) {
                        emitter.onNext(ConnectivityStatus.State.NOT_CONNECTED)
                    }
                }
            }

            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                    .build(),
                networkCallback
            )

            emitter.setCancellable {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }, /* mode */ BackpressureStrategy.LATEST)
    }
}