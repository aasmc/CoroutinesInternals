package ru.aasmc.examples.generator

fun logGenerator() = generate<Unit, String> {
    println("Started with $it")
    println(yield(Unit))
    println(yield(Unit))
    println(yield(Unit))
}

fun main() {
    val gen = logGenerator()
    gen.next("start")
    gen.next("pretzel") // pretzel
    gen.next("california") // california
    gen.next("mayonnaise") // mayonnaise
}