package ru.aasmc.examples.context

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class AuthUser(val name: String): AbstractCoroutineContextElement(AuthUser) {
    companion object Key: CoroutineContext.Key<AuthUser>

    override fun toString(): String = "AuthUser: [name=$name]"
}