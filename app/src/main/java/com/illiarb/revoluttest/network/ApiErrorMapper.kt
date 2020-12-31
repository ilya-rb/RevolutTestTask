package com.illiarb.revoluttest.network

import com.illiarb.revoluttest.libs.tools.ResourceResolver
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ApiErrorMapper @Inject constructor(
    private val resourceResolver: ResourceResolver
) {

    fun fromThrowable(original: Throwable): ApiError {
        return when (original) {
            is HttpException ->
                ApiError.apiError(resourceResolver, original.response()?.errorBody())
            is IOException -> ApiError.networkError(resourceResolver)
            else -> ApiError.unknownError(resourceResolver)
        }
    }
}