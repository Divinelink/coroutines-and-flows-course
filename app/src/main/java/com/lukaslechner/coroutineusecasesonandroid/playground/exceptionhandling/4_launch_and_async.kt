package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(): Unit {

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

//    val deferred = scope.async {
//        delay(200)
//        throw RuntimeException()
//    }
//
//    scope.launch {
//        deferred.await()
//    }

    // Watch what happens when nested coroutines are started with async
    // Even though we are not calling await on the deferred object, the exception is still thrown.
    // The async coroutine will immediately propagate the exception to the parent coroutine.
    scope.launch {
        async {
            delay(200)
            throw RuntimeException()
        }
    }

    /*
    Here it won't be thrown since are not calling await on the deferred object.
    scope.async {
        async {
            delay(200)
            throw RuntimeException()
        }
    }
    */

//    scope.launch {
//        delay(200)
//        throw RuntimeException()
//    }

    Thread.sleep(1000)

}