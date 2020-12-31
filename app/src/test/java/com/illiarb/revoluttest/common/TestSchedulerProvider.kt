package com.illiarb.revoluttest.common

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

class TestSchedulerProvider : SchedulerProvider {

    private val testScheduler = TestScheduler()

    override val io: Scheduler get() = testScheduler
    override val main: Scheduler get() = testScheduler
    override val computation: Scheduler get() = testScheduler
    override val single: Scheduler get() = testScheduler

    fun advanceToNextRateUpdate() {
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
    }

    fun advanceTimeBy(seconds: Long) {
        testScheduler.advanceTimeBy(seconds, TimeUnit.SECONDS)
    }
}