package ru.aasmc.examples.context

import ru.aasmc.examples.run.launch
import ru.aasmc.examples.util.log
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun makeRequest(): String {
    log("Making request...")
    return suspendCoroutine { c ->
        ForkJoinPool.commonPool().execute {
            c.resume("Result of the request")
        }
    }
}

fun display(result: String) {
    log("Displaying result: $result")
}

fun main() {
    launch(Swing) {
        try {
            // suspend while asynchronously making request
            val result = makeRequest()
            // display result in UI, here Swing context ensures that we always stay in EDT
            display(result)
        } catch (e: Throwable) {
            // process exception
        }
    }
}