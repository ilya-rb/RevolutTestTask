package com.illiarb.revoluttest.functional

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.illiarb.revoluttest.App

@Suppress("unused")
class AppRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, App::class.java.name, context)
    }
}