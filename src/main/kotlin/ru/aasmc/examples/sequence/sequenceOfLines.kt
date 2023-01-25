package ru.aasmc.examples.sequence

import java.io.BufferedReader
import java.io.FileReader

fun sequenceOfLines(fileName: String) = sequence<String> {
    BufferedReader(FileReader(fileName)).use {
        while (true) {
            yield(it.readLine() ?: break)
        }
    }
}

fun main() {
    sequenceOfLines("src/main/kotlin/ru/aasmc/examples/mutex/mutex.kt")
        .forEach(::println)
}