package com.illiarb.revoluttest.network

import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.libs.tools.ResourceResolver
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ApiErrorMapper @Inject constructor(
    private val resourceResolver: ResourceResolver
) {

    fun fromThrowable(original: Throwable): ApiError {
        return when (original) {
            is HttpException -> {
                val response = original.response()

                return ApiError(
                    message = resourceResolver.getString(R.string.error_http),
                    kind = ApiError.Kind.HTTP,
                    errorResponse = response?.errorBody()
                )
            }
            is IOException -> {
                return ApiError(
                    message = resourceResolver.getString(R.string.error_io),
                    kind = ApiError.Kind.NETWORK
                )
            }
            else -> ApiError(
                message = resourceResolver.getString(R.string.error_unknown),
                kind = ApiError.Kind.UNKNOWN
            )
        }
    }
}