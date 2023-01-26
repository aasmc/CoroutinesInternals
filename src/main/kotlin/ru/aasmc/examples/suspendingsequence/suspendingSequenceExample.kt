package ru.aasmc.examples.suspendingsequence

import ru.aasmc.examples.context.newSingleThreadContext
import ru.aasmc.examples.delay.delay
import ru.aasmc.examples.run.runBlocking
import ru.aasmc.examples.util.log
import java.util.*

fun main() {
    val context = newSingleThreadContext("MyThread")
    runBlocking(context) {
        // asynchronously generate a number every 500ms
        val seq = suspendingSequence(context) {
            log("Starting generator")
            for (i in 1..10) {
                log("Generator yields $i")
                yield(i)
                val generatorSleep = 500L
                log("Generator goes to sleep for $generatorSleep ms")
                delay(generatorSleep)
            }
            log("Generator is done")
        }
        // simulate async work by sleeping randomly
        val random = Random()
        // consume asynchronous sequence with a regular for loop
        for (value in seq) {
            log("Consumer got value = $value")
            val consumerSleep = random.nextInt(1000).toLong()
            log("Consumer goes to sleep for $consumerSleep ms")
            delay(consumerSleep)
        }
    }
}