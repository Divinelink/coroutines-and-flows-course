package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main(): Unit {

    val scope = CoroutineScope(Job())

    scope.launch {
        try {
            launch {
                functionThatThrows()
            }
        } catch (e: Exception) {
            println("Caught exception on coroutine: $e")
        }
    }

    Thread.sleep(100)

    try {
        functionThatThrows()
    } catch (e: Exception) {
        println("Caught exception: $e")
    }

}

private fun functionThatThrows() {
    throw RuntimeException()
}