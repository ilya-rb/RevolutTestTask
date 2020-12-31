package com.illiarb.revoluttest.network

import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.libs.tools.ResourceResolver

data class ApiError(
    override val message: String,
    val kind: Kind,
    val errorResponse: Any? = null
) : Throwable(message) {

    enum class Kind {
        NETWORK,
        UNKNOWN,
        HTTP
    }

    companion object {

        fun apiError(resourceResolver: ResourceResolver, responseBody: Any?): ApiError {
            return ApiError(
                message = resourceResolver.getString(R.string.error_http),
                kind = Kind.HTTP,
                errorResponse = responseBody
            )
        }

        fun networkError(resourceResolver: ResourceResolver): ApiError {
            return ApiError(
                message = resourceResolver.getString(R.string.error_io),
                kind = Kind.NETWORK
            )
        }

        fun unknownError(resourceResolver: ResourceResolver): ApiError {
            return ApiError(
                message = resourceResolver.getString(R.string.error_unknown),
                kind = Kind.UNKNOWN
            )
        }
    }
}