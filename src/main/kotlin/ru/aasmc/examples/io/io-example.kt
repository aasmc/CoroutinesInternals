package ru.aasmc.examples.io

import ru.aasmc.examples.context.Swing
import ru.aasmc.examples.run.launch
import ru.aasmc.examples.util.log
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Paths

fun main() {
    launch(Swing) {
        val fileName = "src/main/kotlin/ru/aasmc/examples/io/io.kt"
        log("Asynchronously loading file \"$fileName\" ...")
        val channel = AsynchronousFileChannel.open(Paths.get(fileName))
        try {
            val buf = ByteBuffer.allocate(4096)
            val bytesRead = channel.aRead(buf)
            log("Read $bytesRead bytes starting with \"${String(buf.array().copyOf(10))}\"")
        } finally {
            channel.close()
        }
    }
}