package ru.aasmc.examples.future

import java.util.concurrent.CompletableFuture

fun foo(): CompletableFuture<String> = CompletableFuture.supplyAsync { "foo" }
fun bar(v: String): CompletableFuture<String> = CompletableFuture.supplyAsync { "bar with $v" }

fun main() {
    val future = future {
        println("Start")
        val x = foo().await()
        println("got $x")
        val y = bar(x).await()
        println("got $y after $x")
        y
    }
    future.join()
}