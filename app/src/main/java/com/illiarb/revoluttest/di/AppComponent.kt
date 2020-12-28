package com.illiarb.revoluttest.di

import android.content.Context
import com.illiarb.revoluttest.di.modules.NetworkModule
import com.illiarb.revoluttest.di.modules.ApiModule
import com.illiarb.revoluttest.di.modules.ServicesModule
import com.illiarb.revoluttest.libs.tools.di.ToolsModule
import com.illiarb.revoluttest.modules.home.di.HomeComponent
import com.illiarb.revoluttest.modules.main.di.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ApiModule::class,
        ServicesModule::class,
        ToolsModule::class
    ]
)
interface AppComponent : AppProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

interface AppProvider : MainComponent.Dependencies, HomeComponent.Dependencies