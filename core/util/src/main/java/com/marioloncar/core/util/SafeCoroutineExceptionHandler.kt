package com.marioloncar.core.util

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.isActive
import timber.log.Timber
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Creates a [CoroutineExceptionHandler] that only invokes the provided [handler]
 * if the coroutine context is still active. If the context is no longer active,
 * the exception is logged with a warning using Timber.
 */
inline fun safeCoroutineExceptionHandler(
    crossinline handler: (CoroutineContext, Throwable) -> Unit,
): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
        override fun handleException(context: CoroutineContext, exception: Throwable) {
            if (context.isActive) handler(context, exception)
            else Timber.w("Error occurred but the consumer is no longer active", exception)
        }
    }
