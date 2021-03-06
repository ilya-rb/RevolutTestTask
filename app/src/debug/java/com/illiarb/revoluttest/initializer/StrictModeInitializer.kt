package com.illiarb.revoluttest.initializer

import android.app.Application
import android.os.StrictMode
import javax.inject.Inject

class StrictModeInitializer @Inject constructor() : AppInitializer {

    override fun initialize(app: Application) {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites()
                .detectDiskReads()
                .detectNetwork()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .penaltyLog()
                .build()
        )
    }
}