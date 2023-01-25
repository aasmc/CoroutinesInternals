package ru.aasmc.examples.context

import ru.aasmc.examples.delay.delay
import ru.aasmc.examples.future.awaitCustom
import ru.aasmc.examples.future.future
import ru.aasmc.examples.util.log

fun main() {
    log("Starting MyEventThread")
    val context = newSingleThreadContext("MyEventThread")
    val f = future(context) {
        log("Hello, world!")
        val f1 = future(context) {
            log("f1 is sleeping")
            delay(1000L) // sleep 1s
            log("f1 returns 1")
            1
        }
        val f2 = future(context) {
            log("f2 is sleeping")
            delay(1000L) // sleep 1s
            log("f2 returns 2")
            2
        }
        log("I'll wait for both f1 and f2. It should take just a second!")
        val sum = f1.awaitCustom() + f2.awaitCustom()
        log("And the sum is $sum")
    }
    f.get()
    log("Terminated")
}