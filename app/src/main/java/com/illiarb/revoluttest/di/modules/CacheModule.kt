package com.illiarb.revoluttest.di.modules

import com.illiarb.revoluttest.services.revolut.internal.cache.BinaryPrefsRatesCache
import com.illiarb.revoluttest.services.revolut.internal.cache.RatesCache
import dagger.Binds
import dagger.Module

@Module
interface CacheModule {

    @Binds
    fun bindRatesCache(cache: BinaryPrefsRatesCache) : RatesCache
}