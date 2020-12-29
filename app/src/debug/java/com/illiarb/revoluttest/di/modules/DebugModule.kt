package com.illiarb.revoluttest.di.modules

import android.content.Context
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.illiarb.revoluttest.initializer.AppInitializer
import com.illiarb.revoluttest.initializer.FlipperInitializer
import com.illiarb.revoluttest.initializer.StrictModeInitializer
import com.illiarb.revoluttest.qualifier.NetworkInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
interface DebugModule {

    @Multibinds
    fun bindFlipperPlugins(): Set<FlipperPlugin>

    @Binds
    @IntoSet
    fun bindNetworkFlipperPluginIntoSet(plugin: NetworkFlipperPlugin): FlipperPlugin

    @Binds
    @IntoSet
    fun bindStrictModeInitializer(initializer: StrictModeInitializer): AppInitializer

    @Binds
    @IntoSet
    fun bindFlipperInitializer(initializer: FlipperInitializer): AppInitializer

    @Module
    companion object {

        @Provides
        @ElementsIntoSet
        @JvmStatic
        fun provideFlipperPlugins(context: Context): Set<FlipperPlugin> {
            return setOf(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
        }

        @Provides
        @JvmStatic
        @Singleton
        fun provideNetworkFlipperPlugin() = NetworkFlipperPlugin()

        @Provides
        @JvmStatic
        @IntoSet
        @NetworkInterceptor
        fun provideFlipperInterceptor(plugin: NetworkFlipperPlugin): Interceptor {
            return FlipperOkhttpInterceptor(plugin)
        }
    }
}