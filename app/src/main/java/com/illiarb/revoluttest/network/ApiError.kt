package com.illiarb.revoluttest.network

data class ApiError(override val message: String) : Throwable(message)