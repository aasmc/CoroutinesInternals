package ru.aasmc.examples.channel

import ru.aasmc.examples.context.CommonPool
import ru.aasmc.examples.run.runBlocking

fun mainBlocking(block: suspend () -> Unit) = runBlocking(CommonPool, block)

fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)