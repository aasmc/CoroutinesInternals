package ru.aasmc.examples.delay

import ru.aasmc.examples.util.log
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler").apply { isDaemon = true }
}

suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): Unit = suspendCoroutine { cont ->
    log("In delay")
    executor.schedule({ log("Exiting delay... "); cont.resume(Unit) }, time, unit)
}