package ru.aasmc.examples.channel

import ru.aasmc.examples.delay.delay
import java.time.Instant

object Time {
    fun tick(millis: Long): ReceiveChannel<Instant> {
        val c = Channel<Instant>()
        go {
            while (true) {
                delay(millis)
                c.send(Instant.now())
            }
        }
        return c
    }

    fun after(millis: Long): ReceiveChannel<Instant> {
        val c = Channel<Instant>()
        go {
            delay(millis)
            c.send(Instant.now())
            c.close()
        }
        return c
    }
}