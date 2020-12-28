package com.illiarb.revoluttest.modules.main.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.di.ViewModelModule
import com.illiarb.revoluttest.modules.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [MainModule::class, ViewModelModule::class],
    dependencies = [MainComponent.Dependencies::class]
)
interface MainComponent {

    interface Dependencies {
        fun connectivityStatus(): ConnectivityStatus
        fun schedulerProvider(): SchedulerProvider
    }

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance activity: FragmentActivity,
            dependencies: Dependencies
        ): MainComponent
    }

    fun inject(activity: MainActivity)
}