package ru.aasmc.examples.run

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
    /**
     * Starts a coroutine without a receiver and with result type [T].
     * This function creates and starts a new, fresh instance of suspendable
     * computation every time it is invoked.
     * The [completion] continuation is invoked when the coroutine
     * completes with a result or an exception.
     */
    block.startCoroutine(Continuation(context) { result ->
        result.onFailure { e ->
            val currentThread = Thread.currentThread()
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, e)
        }
    })