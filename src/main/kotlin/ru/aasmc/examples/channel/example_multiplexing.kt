package ru.aasmc.examples.channel

suspend fun fanIn(input1: ReceiveChannel<String>, input2: ReceiveChannel<String>): ReceiveChannel<String> {
    val c = Channel<String>()
    go {
        for (v in input1)
            c.send(v)
    }
    go {
        for (v in input2)
            c.send(v)
    }
    return c // return combo channel
}

fun main(args: Array<String>) = mainBlocking {
    val c = fanIn(boring("Joe"), boring("Ann"))
    for (i in 0..9) {
        println(c.receive())
    }
    println("Your're both boring; I'm leaving.")
}
