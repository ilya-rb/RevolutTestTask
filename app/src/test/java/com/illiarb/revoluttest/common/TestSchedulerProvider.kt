package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler

class TestSchedulerProvider : SchedulerProvider {

    val testScheduler = TestScheduler()

    override val io: Scheduler get() = testScheduler
    override val main: Scheduler get() = testScheduler
    override val computation: Scheduler get() = testScheduler
    override val single: Scheduler get() = testScheduler
}