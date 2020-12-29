package com.illiarb.revoluttest.di.modules

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.network.ApiErrorMapper
import com.illiarb.revoluttest.network.RxCallAdapterFactory
import com.illiarb.revoluttest.qualifier.NetworkInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    private const val TIMEOUT_SECONDS = 10L

    @Provides
    @JvmStatic
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @JvmStatic
    fun provideCallAdapterFactory(
        schedulerProvider: SchedulerProvider,
        errorCreator: ApiErrorMapper
    ): CallAdapter.Factory = RxCallAdapterFactory(schedulerProvider, errorCreator)

    @Provides
    @JvmStatic
    fun provideApiConverterFactory(moshi: Moshi): Converter.Factory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @JvmStatic
    fun provideOkHttpClient(
        @NetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .apply {
                networkInterceptors.forEach {
                    addNetworkInterceptor(it)
                }
            }
            .build()
}