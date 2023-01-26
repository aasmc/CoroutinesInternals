package ru.aasmc.examples.suspendingsequence

import kotlin.coroutines.*
import kotlin.experimental.ExperimentalTypeInference

interface SuspendingSequenceScope<in T> {
    suspend fun yield(value: T)
}

interface SuspendingSequence<out T> {
    operator fun iterator(): SuspendingIterator<T>
}

interface SuspendingIterator<out T> {
    suspend operator fun hasNext(): Boolean
    suspend operator fun next(): T
}

@OptIn(ExperimentalTypeInference::class)
fun <T> suspendingSequence(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend SuspendingSequenceScope<T>.() -> Unit
): SuspendingSequence<T> = object : SuspendingSequence<T> {
    override fun iterator(): SuspendingIterator<T> = suspendingIterator(context, block)
}

fun <T> suspendingIterator(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend SuspendingSequenceScope<T>.() -> Unit
): SuspendingIterator<T> =
    SuspendingIteratorCoroutine<T>(context).apply {
        nextStep = block.createCoroutine(receiver = this, completion = this)
    }

class SuspendingIteratorCoroutine<T>(
    override val context: CoroutineContext
) : SuspendingIterator<T>, SuspendingSequenceScope<T>, Continuation<Unit> {
    enum class State { INITIAL, COMPUTING_HAS_NEXT, COMPUTING_NEXT, COMPUTED, DONE }

    var state: State = State.INITIAL
    var nextValue: T? = null
    var nextStep: Continuation<Unit>? = null // null when sequence complete

    // if (state == COMPUTING_NEXT) computeContinuation is Continuation<T>
    // if (state == COMPUTING_HAS_NEXT) computeContinuation is Continuation<Boolean>
    var computeContinuation: Continuation<*>? = null

    suspend fun computeHasNext(): Boolean = suspendCoroutine { c ->
        state = State.COMPUTING_HAS_NEXT
        computeContinuation = c
        nextStep!!.resume(Unit)
    }

    suspend fun computeNext(): T = suspendCoroutine { c ->
        state = State.COMPUTING_NEXT
        computeContinuation = c
        nextStep!!.resume(Unit)
    }

    override suspend fun hasNext(): Boolean {
        return when (state) {
            State.INITIAL -> computeHasNext()
            State.COMPUTED -> true
            State.DONE -> false
            else -> throw IllegalStateException(
                "recursive dependency detected -- " +
                        "already computing next"
            )
        }
    }

    override suspend fun next(): T {
        return when (state) {
            State.INITIAL -> computeNext()
            State.COMPUTED -> {
                state = State.INITIAL
                @Suppress("UNCHECKED_CAST")
                nextValue as T
            }
            State.DONE -> throw NoSuchElementException()
            else -> throw IllegalStateException(
                "Recursive dependency detected -- " +
                        "already computing next"
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun resumeIterator(hasNext: Boolean) {
        when (state) {
            State.COMPUTING_HAS_NEXT -> {
                state = State.COMPUTED
                (computeContinuation as Continuation<Boolean>).resume(hasNext)
            }
            State.COMPUTING_NEXT -> {
                state = State.INITIAL
                (computeContinuation as Continuation<T>).resume(nextValue as T)
            }
            else -> throw IllegalStateException(
                "Was not supposed to be computing next value. " +
                        "Spurious yield?"
            )
        }
    }

    // completion continuation impl
    override fun resumeWith(result: Result<Unit>) {
        nextStep = null
        result
            .onSuccess {
                resumeIterator(false)
            }
            .onFailure {
                state = State.DONE
                computeContinuation!!.resumeWithException(it)
            }
    }

    // generator impl
    override suspend fun yield(value: T): Unit = suspendCoroutine { c ->
        nextValue = value
        nextStep = c
        resumeIterator(true)
    }
}

































