package com.illiarb.revoluttest.libs.tools.di

import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.ResourceResolver
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.tools.internal.AndroidConnectivityStatus
import com.illiarb.revoluttest.libs.tools.internal.AndroidResourceResolver
import com.illiarb.revoluttest.libs.tools.internal.RxSchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ToolsModule {

    @Binds
    internal abstract fun bindConnectivityStatus(
        connectivityStatus: AndroidConnectivityStatus
    ): ConnectivityStatus

    @Binds
    internal abstract fun bindSchedulerProvider(
        schedulerProvider: RxSchedulerProvider
    ): SchedulerProvider

    @Binds
    internal abstract fun bindResourceResolver(
        resourceResolver: AndroidResourceResolver
    ) : ResourceResolver
}