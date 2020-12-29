package com.illiarb.revoluttest

import android.app.Application
import com.illiarb.revoluttest.di.AppComponent
import com.illiarb.revoluttest.di.DaggerAppComponent
import com.illiarb.revoluttest.initializer.AppInitializer
import javax.inject.Inject

// Used in AndroidManifest.xml
@Suppress("unused")
class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(context = this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks(appComponent))
        appComponent.inject(this)
    }

    @Inject
    @Suppress("unused", "ProtectedInFinal")
    protected fun runInitializers(initializers: Set<@JvmSuppressWildcards AppInitializer>) {
        initializers.forEach {
            it.initialize(app = this)
        }
    }
}