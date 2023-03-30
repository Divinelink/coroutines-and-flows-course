package com.lukaslechner.coroutineusecasesonandroid.playground.utils

import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> retry(
    numberOfRetries: Int,
    initialDelayMillis: Long = 100,
    maxDelayMillis: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T,
): T {
    var currentMillis = initialDelayMillis
    repeat(numberOfRetries) {
        try {
            return block()
        } catch (e: Exception) {
            Timber.e(e)
        }
        delay(currentMillis)
        currentMillis = (currentMillis * factor).toLong().coerceAtMost(maxDelayMillis)
    }
    return block()
}