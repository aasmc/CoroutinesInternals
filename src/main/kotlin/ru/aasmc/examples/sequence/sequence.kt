package ru.aasmc.examples.sequence

import kotlin.coroutines.*
import kotlin.experimental.ExperimentalTypeInference

@RestrictsSuspension
interface SequenceScope<in T> {
    suspend fun yield(value: T)
}

@OptIn(ExperimentalTypeInference::class)
fun <T> sequence(@BuilderInference block: suspend SequenceScope<T>.() -> Unit): Sequence<T> = Sequence {
    SequenceCoroutine<T>().apply {
        /**
         * Creates a coroutine with receiver type [R] and result type [T].
         * This function creates a new, fresh instance of suspendable computation every time
         * it is invoked.
         *
         * To start executing the created coroutine, invoke `resume(Unit)` on the returned
         * [Continuation] instance.
         * The [completion] continuation is invoked when the coroutine completes with a
         * result or an exception.
         * Subsequent invocation of any resume function on the resulting continuation will
         * produce an [IllegalStateException].
         */
        nextStep = block.createCoroutine(receiver = this, completion = this)
    }
}

private class SequenceCoroutine<T> : AbstractIterator<T>(), SequenceScope<T>, Continuation<Unit> {
    lateinit var nextStep: Continuation<Unit>

    // AbstractIterator implementation
    override fun computeNext() {
        nextStep.resume(Unit)
    }

    // Completion continuation implementation
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow() // bail out on error
        done()
    }

    // Generator implementation
    override suspend fun yield(value: T) {
        setNext(value)
        return suspendCoroutine { cont -> nextStep = cont }
    }
}