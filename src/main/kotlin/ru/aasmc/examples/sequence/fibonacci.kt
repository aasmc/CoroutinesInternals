package ru.aasmc.examples.sequence

import java.math.BigInteger

val fibonacci = sequence {
    yield(BigInteger.ONE)
    var cur = BigInteger.ONE
    var next = BigInteger.ONE
    while (true) {
        yield(next) // next Fibonacci number
        val tmp = cur + next
        cur = next
        next = tmp
    }
}

fun main() {
    println(fibonacci.take(10).joinToString(separator = ", \n"))
}