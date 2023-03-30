package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val job = launch {
        repeat(1000) { i ->
            println("operation number $i ...")
            delay(100L)
        }
    }

    delay(550)
    println("Cancelling job...")
    job.cancel()
}