package com.illiarb.revoluttest.libs.tools

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {

    val io: Scheduler

    val main: Scheduler

    val computation: Scheduler

    val single: Scheduler
}