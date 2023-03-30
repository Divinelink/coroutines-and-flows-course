package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*


fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { threadSwitchingCoroutine(1, 500) },
        async { threadSwitchingCoroutine(2, 300) },
    )
}

suspend fun threadSwitchingCoroutine(
    number: Int,
    delay: Long,
) {
    println("Coroutine $number starts work on thread ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.Default) {
        println("Coroutine $number switches to thread ${Thread.currentThread().name}")
    }
}