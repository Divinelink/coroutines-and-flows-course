package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

fun main(): Unit {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught exception: $throwable in CoroutineExceptionHandler!")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        try {
            supervisorScope {
                launch {
                    println("CEH: ${coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
            }
        } catch (e: Exception) {
            println("Caught exception: $e")
        }
    }

    Thread.sleep(1000)

}