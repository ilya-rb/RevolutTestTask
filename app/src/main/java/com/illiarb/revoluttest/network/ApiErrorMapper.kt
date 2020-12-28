package com.illiarb.revoluttest.network

import com.illiarb.revoluttest.libs.tools.ResourceResolver
import okhttp3.ResponseBody
import javax.inject.Inject

// TODO:
class ApiErrorMapper @Inject constructor(
    private val resourceResolver: ResourceResolver
) {

    fun mapFromThrowable(original: Throwable): ApiError {
        return ApiError(original.message ?: "")
    }

    fun mapFromErrorBody(responseBody: ResponseBody?): ApiError {
        return ApiError("")
    }
}