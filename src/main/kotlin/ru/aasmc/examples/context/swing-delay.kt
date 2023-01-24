package ru.aasmc.examples.context

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import javax.swing.Timer


suspend fun Swing.delay(millis: Int): Unit = suspendCoroutine { cont ->
    Timer(millis) { cont.resume(Unit) }.apply {
        isRepeats = false
        start()
    }
}