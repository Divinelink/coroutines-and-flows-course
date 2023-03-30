package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines_builders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val startTime = System.currentTimeMillis()
/*
    val resultList = mutableListOf<String>()

    val job1 = launch {
        val result1 = networkCall(1)
        resultList.add(result1)
        println("result $result1 completed in ${elapsedTime(startTime)} ms")
    }
    val job2 = launch {
        val result2 = networkCall(2)
        resultList.add(result2)
        println("result $result2 completed in ${elapsedTime(startTime)} ms")
    }

    job1.join()
    job2.join()

    println(resultList)
 */

    val deferred1 = async {
        val result1 = networkCall(1)
        println("result $result1 completed in ${elapsedTime(startTime)} ms")
        result1
    }

    val deferred2 = async {
        val result2 = networkCall(2)
        println("result $result2 completed in ${elapsedTime(startTime)} ms")
        result2
    }

    val resultList = listOf(deferred1.await(), deferred2.await())

    println("Results: $resultList after ${elapsedTime(startTime)} ms")
}

suspend fun networkCall(
    number: Int,
): String {
    delay(500)
    return "Result $number"
}

fun elapsedTime(startTime: Long): Long {
    return System.currentTimeMillis() - startTime
}