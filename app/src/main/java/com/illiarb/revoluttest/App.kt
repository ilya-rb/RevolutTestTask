package com.illiarb.revoluttest

import android.app.Application
import com.illiarb.revoluttest.di.AppComponent
import com.illiarb.revoluttest.di.DaggerAppComponent
import timber.log.Timber

// Used in AndroidManifest.xml
@Suppress("unused")
class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(context = this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks(appComponent))
        Timber.plant(Timber.DebugTree())
    }
}