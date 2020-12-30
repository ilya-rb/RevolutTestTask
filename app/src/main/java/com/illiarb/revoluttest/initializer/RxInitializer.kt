package com.illiarb.revoluttest.initializer

import android.app.Application
import android.os.Looper
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

class RxInitializer @Inject constructor() : AppInitializer {

    override fun initialize(app: Application) {
        RxJavaPlugins.setErrorHandler(Timber::e)

        val asyncMainThreadScheduler =
            AndroidSchedulers.from(Looper.getMainLooper(), /* async */ true)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }
    }
}