package ru.aasmc.examples.context

import ru.aasmc.examples.run.launch
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.*

object CommonPool : Pool(ForkJoinPool.commonPool())

open class Pool(val pool: ForkJoinPool) :
    AbstractCoroutineContextElement(ContinuationInterceptor),
    /**
     * Marks coroutine context element that intercepts coroutine continuations.
     * The coroutines framework uses [ContinuationInterceptor.Key] to retrieve the interceptor and
     * intercepts all coroutine continuations with [interceptContinuation] invocations.
     *
     * [ContinuationInterceptor] behaves like a [polymorphic element][AbstractCoroutineContextKey],
     * meaning that
     * its implementation delegates [get][CoroutineContext.Element.get] and
     * [minusKey][CoroutineContext.Element.minusKey]
     * to [getPolymorphicElement] and [minusPolymorphicKey] respectively.
     * [ContinuationInterceptor] subtypes can be extracted from the coroutine context using
     * either [ContinuationInterceptor.Key]
     * or subtype key if it extends [AbstractCoroutineContextKey].
     */
    ContinuationInterceptor {

    /**
     * Returns continuation that wraps the original [continuation], thus intercepting all resumptions.
     * This function is invoked by coroutines framework when needed and the resulting continuations are
     * cached internally per each instance of the original [continuation].
     *
     * This function may simply return original [continuation] if it does not want to intercept
     * this particular continuation.
     *
     * When the original [continuation] completes, coroutine framework invokes
     * [releaseInterceptedContinuation]
     * with the resulting continuation if it was intercepted, that is if `interceptContinuation`
     * had previously
     * returned a different continuation instance.
     */
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        PoolContinuation(pool, continuation.context.fold(continuation) { cont, element ->
            if (element != this@Pool && element is ContinuationInterceptor) {
                element.interceptContinuation(cont)
            } else {
                cont
            }
        })

    fun runParallel(block: suspend () -> Unit) {
        pool.execute { launch(this, block) }
    }

}

private class PoolContinuation<T>(
    val pool: ForkJoinPool,
    val cont: Continuation<T>
) : Continuation<T> {
    override val context: CoroutineContext = cont.context

    override fun resumeWith(result: Result<T>) {
        pool.execute { cont.resumeWith(result) }
    }
}