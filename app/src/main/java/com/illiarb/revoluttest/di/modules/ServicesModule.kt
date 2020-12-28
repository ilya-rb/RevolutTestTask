package com.illiarb.revoluttest.di.modules

import com.illiarb.revoluttest.services.revolut.RatesService
import com.illiarb.revoluttest.services.revolut.internal.RevolutRatesService
import dagger.Binds
import dagger.Module

@Module
internal interface ServicesModule {

    @Binds
    fun bindRatesService(service: RevolutRatesService): RatesService
}