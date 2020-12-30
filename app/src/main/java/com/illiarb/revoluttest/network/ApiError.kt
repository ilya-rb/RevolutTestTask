package com.illiarb.revoluttest.network

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
}