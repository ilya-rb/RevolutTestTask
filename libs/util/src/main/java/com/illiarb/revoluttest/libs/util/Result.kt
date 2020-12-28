package com.illiarb.revoluttest.libs.util

sealed class Result<out T> {

    class Ok<T>(val data: T) : Result<T>()

    class Err(val error: Throwable) : Result<Nothing>()

    fun unwrap(): T = when (this) {
        is Ok -> data
        is Err -> throw error
    }

    companion object {

        @Suppress("TooGenericExceptionCaught")
        inline fun <T : Any> create(block: () -> T): Result<T> {
            return try {
                Ok(block())
            } catch (e: Throwable) {
                Err(e)
            }
        }
    }
}