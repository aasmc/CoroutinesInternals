package ru.aasmc.examples.sequence.optimized

val fibonacci: Sequence<Int> = sequenceOptimized {
    yield(1)
    var cur = 1
    var next = 1
    while (true) {
        yield(next)
        val tmp = cur + next
        cur = next
        next = tmp
    }
}

fun main() {
    println(fibonacci)
    println(fibonacci.take(10).joinToString(separator = ", "))
}