package ru.aasmc.examples.delay

import ru.aasmc.examples.context.Swing
import ru.aasmc.examples.future.future
import ru.aasmc.examples.util.log

fun main() {
    future(Swing) {
        log("Let's natively sleep for 1 second")
        delay(1000L)
        log("We're still in Swing EDT!")
    }.join()
}