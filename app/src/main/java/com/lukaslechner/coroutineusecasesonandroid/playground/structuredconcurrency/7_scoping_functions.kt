package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val scope = CoroutineScope(context = Job())

    scope.launch {

        /*
        val job1 = launch {
            println("Starting Task 1")
            delay(100)
            println("Task 1 Completed")
        }

        val job2 = launch {
            println("Starting Task 2")
            delay(200)
            println("Task 2 Completed")
        }

        job1.join()
        job2.join()

        launch {
            println("Starting Task 3")
            delay(3000)
            println("Task 3 Completed")
        }
         */

        // The following code is equivalent to the above commented code
        doSomeTasks()

        launch {
            println("Starting Task 3")
            delay(300)
            println("Task 3 Completed")
        }


    }.join()

}

/**
 * This will run the tasks concurrently with the other tasks in the scope.
 * This is not so correct since concurrency should be explicit.
 * So concurrency should not be the default.
 * Therefore it's better to use the suspend function [doSomeTasks] instead,
 * and if we want to run the tasks concurrently, we run the function in a coroutine that runs concurrently.
 */
fun CoroutineScope.doSomeTasksConcurrently() {
    launch {
        println("Starting Task 1")
        delay(100)
        println("Task 1 Completed")
    }

    launch {
        println("Starting Task 2")
        delay(200)
        println("Task 2 Completed")
    }
}

/**
 * This will run the tasks sequentially with the other tasks in the scope.
 */
suspend fun doSomeTasks() = coroutineScope {
    launch {
        println("Starting Task 1")
        delay(100)
        println("Task 1 Completed")
    }

    launch {
        println("Starting Task 2")
        delay(200)
        println("Task 2 Completed")
    }
}