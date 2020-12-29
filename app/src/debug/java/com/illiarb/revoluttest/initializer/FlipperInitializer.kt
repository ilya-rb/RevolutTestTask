package com.illiarb.revoluttest.initializer

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.soloader.SoLoader
import javax.inject.Inject

class FlipperInitializer @Inject constructor(
    private val plugins: Set<@JvmSuppressWildcards FlipperPlugin>
) : AppInitializer {

    override fun initialize(app: Application) {
        SoLoader.init(app, /* nativeExoPackage */ false)

        val client = AndroidFlipperClient.getInstance(app)

        plugins.forEach(client::addPlugin)

        client.start()
    }
}