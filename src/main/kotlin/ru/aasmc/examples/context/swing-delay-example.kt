package ru.aasmc.examples.context

import ru.aasmc.examples.future.future
import ru.aasmc.examples.util.log

fun main() {
    future(Swing) {
        log("Let's Swing.delay for 1 second")
        Swing.delay(1000)
        log("We're still in Swing EDT")
    }
}