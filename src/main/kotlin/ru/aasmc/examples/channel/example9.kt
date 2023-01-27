package ru.aasmc.examples.channel

import ru.aasmc.examples.delay.delay
import ru.aasmc.examples.mutex.Mutex

class SafeCounter {
    private val v = mutableMapOf<String, Int>()
    private val mux = Mutex()

    suspend fun inc(key: String) {
        mux.lock()
        try { v[key] = v.getOrDefault(key, 0) + 1 }
        finally { mux.unlock() }
    }

    suspend fun get(key: String): Int? {
        mux.lock()
        return try { v[key] }
        finally { mux.unlock() }
    }
}

fun main(args: Array<String>) = mainBlocking {
    val c = SafeCounter()
    for (i in 0..999) {
        go { c.inc("somekey") } // 1000 concurrent coroutines
    }
    delay(1000)
    println("${c.get("somekey")}")
}
