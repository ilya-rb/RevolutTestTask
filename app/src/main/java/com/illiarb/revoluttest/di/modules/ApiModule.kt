package com.illiarb.revoluttest.di.modules

import com.illiarb.revoluttest.services.revolut.internal.api.LatestRatesApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.Lazy
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

@Module
object ApiModule {

    @Provides
    @JvmStatic
    internal fun provideLatestRatesApi(retrofit: Retrofit): LatestRatesApi =
        retrofit.create(LatestRatesApi::class.java)

    @Provides
    @JvmStatic
    @Reusable
    internal fun provideRetrofit(
        okHttpClient: Lazy<OkHttpClient>,
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            // TODO: Move out to build config
            .baseUrl("https://hiring.revolut.codes/api/android/")
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .callFactory { okHttpClient.get().newCall(it) }
            .build()
}