package ru.aasmc.examples.generator

fun idMaker(): Generator<Int, Unit> = generate<Int, Unit> {
    var index = 0
    while (index < 3) {
        yield(index++)
    }
}

fun main() {
    val gen = idMaker()
    println(gen.next(Unit)) // 0
    println(gen.next(Unit)) // 1
    println(gen.next(Unit)) // 2
    println(gen.next(Unit)) // null
}