package ru.aasmc.examples.util

import java.time.Instant

fun log(msg: String) = println("${Instant.now()} [${Thread.currentThread().name}] $msg")