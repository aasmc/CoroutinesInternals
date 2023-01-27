package ru.aasmc.examples.channel

import ru.aasmc.examples.delay.delay

suspend fun say(s: String) {
    for (i in 0..4) {
        delay(100)
        println(s)
    }
}

fun main() = mainBlocking {
    go { say("world") }
    say("hello")
}