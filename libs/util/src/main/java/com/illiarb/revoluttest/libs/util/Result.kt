package com.illiarb.revoluttest.libs.util

sealed class Result<out T> {

    class Ok<T>(val data: T) : Result<T>()

    class Err(val error: Throwable) : Result<Nothing>()

    fun doIfOk(block: (T) -> Unit) {
        if (this is Ok) {
            block(data)
        }
    }
}