package com.illiarb.revoluttest.di.modules

import com.illiarb.revoluttest.initializer.AppInitializer
import com.illiarb.revoluttest.initializer.LoggerInitializer
import com.illiarb.revoluttest.initializer.RxInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface InitializersModule {

    @Binds
    @IntoSet
    fun bindLoggerInitializer(initializer: LoggerInitializer): AppInitializer

    @Binds
    @IntoSet
    fun bindRxInitializer(initializer: RxInitializer) : AppInitializer
}