package com.illiarb.revoluttest.services.revolut.internal.api

import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface LatestRatesApi {

    @GET("latest")
    fun latest(@Query("base") baseCurrency: String?): Flowable<LatestRatesResponse>
}