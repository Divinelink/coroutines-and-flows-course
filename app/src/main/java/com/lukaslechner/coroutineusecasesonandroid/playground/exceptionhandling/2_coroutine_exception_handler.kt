package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main(): Unit {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught exception: $throwable")
    }


//    val scope = CoroutineScope(Job() + exceptionHandler)
    val scope = CoroutineScope(Job())

    scope.launch(exceptionHandler) {
        throw RuntimeException()
    }

    Thread.sleep(100)

}

private fun functionThatThrows() {
    throw RuntimeException()
}