package ru.aasmc.examples.channel

import kotlin.system.measureTimeMillis

suspend fun sum2(s: List<Int>, c: SendChannel<Int>) {
    // simulate long-running CPU-consuming computation
    var sum = 0
    val time = measureTimeMillis {
        repeat(100_000_000) {
            for (v in s) {
                sum += v
            }
        }
        c.send(sum)
    }
    println("Sum took $time ms")
}

fun main(args: Array<String>) = mainBlocking {
    val s = listOf(7, 2, 8, -9, 4, 0)
    val c = Channel<Int>()
    go { sum2(s.subList(s.size /2, s.size), c) }
    go { sum2(s.subList(0, s.size / 2), c) }
    val time = measureTimeMillis {
        val x = c.receive()
        val y = c.receive()
        println("$x $y ${x + y}")
    }
    println("Main code took $time ms")
}