package com.illiarb.revoluttest.libs.util

sealed class Optional<out T> {

    class Some<out T>(val element: T): Optional<T>()

    object None: Optional<Nothing>()
}