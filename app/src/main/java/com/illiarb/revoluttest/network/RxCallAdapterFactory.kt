package com.illiarb.revoluttest.network

import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

class RxCallAdapterFactory @Inject constructor(
    schedulerProvider: SchedulerProvider,
    private val errorMapper: ApiErrorMapper
) : CallAdapter.Factory() {

    private val original =
        RxJava3CallAdapterFactory.createWithScheduler(schedulerProvider.io)

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(wrapped, errorMapper)
    }

    private class RxCallAdapterWrapper<R>(
        private val wrappedCallAdapter: CallAdapter<R, *>,
        private val errorMapper: ApiErrorMapper
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type = wrappedCallAdapter.responseType()

        @Suppress("UNCHECKED_CAST")
        override fun adapt(call: Call<R>): Any {
            return when (val adapted = wrappedCallAdapter.adapt(call)) {
                is Observable<*> ->
                    adapted.onErrorResumeNext { Observable.error(errorMapper.mapFromThrowable(it)) }
                is Single<*> ->
                    adapted.onErrorResumeNext { Single.error(errorMapper.mapFromThrowable(it)) }
                is Completable ->
                    adapted.onErrorResumeNext { Completable.error(errorMapper.mapFromThrowable(it)) }
                is Maybe<*> ->
                    adapted.onErrorResumeNext { Maybe.error(errorMapper.mapFromThrowable(it)) }
                is Flowable<*> ->
                    adapted.onErrorResumeNext { Flowable.error(errorMapper.mapFromThrowable(it)) }
                else -> adapted
            }
        }
    }
}