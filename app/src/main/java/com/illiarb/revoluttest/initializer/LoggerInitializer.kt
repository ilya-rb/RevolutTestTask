package com.illiarb.revoluttest.initializer

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.illiarb.revoluttest.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class LoggerInitializer @Inject constructor() : AppInitializer {

    override fun initialize(app: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FirebaseCrashlyticsTree())
        }
    }

    private class FirebaseCrashlyticsTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            with(FirebaseCrashlytics.getInstance()) {
                log(message)
                t?.let(this::recordException)
            }
        }
    }
}