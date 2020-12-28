package com.illiarb.revoluttest.modules.home.di

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.di.ViewModelModule
import com.illiarb.revoluttest.modules.home.HomeFragment
import com.illiarb.revoluttest.services.revolut.RatesService
import dagger.Component

@Component(
    modules = [HomeModule::class, ViewModelModule::class],
    dependencies = [HomeComponent.Dependencies::class]
)
interface HomeComponent {

    interface Dependencies {
        fun ratesService(): RatesService
        fun schedulerProvider(): SchedulerProvider
    }

    @Component.Factory
    interface Factory {
        fun create(dependencies: Dependencies): HomeComponent
    }

    fun inject(fragment: HomeFragment)
}