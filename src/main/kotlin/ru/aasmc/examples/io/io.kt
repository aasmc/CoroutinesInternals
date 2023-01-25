package ru.aasmc.examples.io

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun AsynchronousFileChannel.aRead(buf: ByteBuffer): Int =
    suspendCoroutine { cont ->
        read(buf, 0L, Unit, object : CompletionHandler<Int, Unit> {
            override fun completed(result: Int, attachment: Unit) {
                cont.resume(result)
            }

            override fun failed(exc: Throwable, attachment: Unit) {
                cont.resumeWithException(exc)
            }
        })
    }