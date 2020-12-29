package com.illiarb.revoluttest.initializer

import android.app.Application

interface AppInitializer {

    fun initialize(app: Application)
}