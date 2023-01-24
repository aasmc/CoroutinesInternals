package ru.aasmc.examples.context

import ru.aasmc.examples.run.runBlocking
import ru.aasmc.examples.util.log
import kotlin.coroutines.coroutineContext

suspend fun doSomething() {
    val currentUser = coroutineContext[AuthUser]?.name ?: throw SecurityException("unauthorized")
    log("Current user is $currentUser")
}

fun main() {
    runBlocking(AuthUser("admin")) {
        doSomething()
    }
}