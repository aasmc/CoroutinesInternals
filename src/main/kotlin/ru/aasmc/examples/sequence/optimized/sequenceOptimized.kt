package ru.aasmc.examples.sequence.optimized

import ru.aasmc.examples.sequence.SequenceScope
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

fun <T> sequenceOptimized(block: suspend SequenceScope<T>.() -> Unit): Sequence<T> = Sequence {
    SequenceCoroutine<T>().apply {
        nextStep = block.createCoroutineUnintercepted(receiver = this, completion = this)
    }
}

class SequenceCoroutine<T> : AbstractIterator<T>(), SequenceScope<T>, Continuation<Unit> {
    lateinit var nextStep: Continuation<Unit>

    // AbstractIterator impl
    override fun computeNext() {
        nextStep.resume(Unit)
    }

    // completion continuation impl
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        result.getOrThrow()
        done() // abstractIterator method
    }

    // Generator impl
    override suspend fun yield(value: T) {
        setNext(value) // abstract iterator method
        /**
         * Obtains the current continuation instance inside suspend functions and either suspends
         * currently running coroutine or returns result immediately without suspension.
         *
         * If the [block] returns the special [COROUTINE_SUSPENDED] value, it means that suspend
         * function did suspend the execution and will
         * not return any result immediately. In this case, the [Continuation] provided to the
         * [block] shall be
         * resumed by invoking [Continuation.resumeWith] at some moment in the
         * future when the result becomes available to resume the computation.
         *
         * Otherwise, the return value of the [block] must have a type assignable to [T] and
         * represents the result of this suspend function.
         * It means that the execution was not suspended and the [Continuation] provided to the
         * [block] shall not be invoked.
         * As the result type of the [block] is declared as `Any?` and cannot be correctly type-checked,
         * its proper return type remains on the conscience of the suspend function's author.
         *
         * Invocation of [Continuation.resumeWith] resumes coroutine directly in the invoker's
         * thread without going through the
         * [ContinuationInterceptor] that might be present in the coroutine's [CoroutineContext].
         * It is the invoker's responsibility to ensure that a proper invocation context is established.
         * [Continuation.intercepted] can be used to acquire the intercepted continuation.
         *
         * Note that it is not recommended to call either [Continuation.resume] nor
         * [Continuation.resumeWithException] functions synchronously
         * in the same stackframe where suspension function is run. Use [suspendCoroutine] as a
         * safer way to obtain current
         * continuation instance.
         */
        return suspendCoroutineUninterceptedOrReturn { cont ->
            nextStep = cont
            COROUTINE_SUSPENDED
        }
    }

}