package com.illiarb.revoluttest.libs.tools.internal

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class RxSchedulerProvider @Inject constructor() : SchedulerProvider {

    override val io: Scheduler
        get() = Schedulers.io()

    override val computation: Scheduler
        get() = Schedulers.computation()

    override val single: Scheduler
        get() = Schedulers.single()

    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}