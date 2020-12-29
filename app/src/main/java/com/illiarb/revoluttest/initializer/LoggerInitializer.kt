package com.illiarb.revoluttest.initializer

import android.app.Application
import com.illiarb.revoluttest.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class LoggerInitializer @Inject constructor() : AppInitializer {

    override fun initialize(app: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}