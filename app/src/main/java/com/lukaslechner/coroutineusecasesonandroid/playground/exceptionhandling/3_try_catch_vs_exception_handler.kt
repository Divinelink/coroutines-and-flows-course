package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(): Unit {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught exception: $throwable")
    }

    val scope = CoroutineScope(Job())

    scope.launch(exceptionHandler) {

        launch {
            println("Starting coroutine 1")
            delay(100)
            throw RuntimeException()
//            try {
//                throw RuntimeException()
//            } catch (e: Exception) {
//                println("Caught exception on coroutine 1: $e")
//            }
        }

        launch {
            println("Starting coroutine 2")
            delay(3000)
            println("Coroutine 2 finished")
        }

    }

    Thread.sleep(5000)

}
