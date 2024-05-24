package com.marioloncar.core.util.extension

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock.System
import timber.log.Timber
import kotlin.coroutines.coroutineContext

private const val STOP_TIMEOUT_MILLIS = 1000L

/**
 * Only proceed with the given action if the coroutine has not been cancelled.
 * Necessary because Flow.collect receives items even after coroutine was cancelled.
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1265
 */
suspend inline fun <T> Flow<T>.safeCollect(crossinline action: suspend (T) -> Unit) {
    collect {
        if (coroutineContext.isActive) action(it)
    }
}

/**
 * Returns flow containing only distinct elements.
 */
fun <T> Flow<T>.distinct(): Flow<T> {
    val appeared = mutableSetOf<T>()
    return filter(appeared::add)
}

/**
 * Returns a Flow that emits only the first item emitted by the source Flow during
 * sequential time windows of a specified duration.
 */
fun <T> Flow<T>.throttleFirst(skipDuration: Long): Flow<T> = flow {
    var lastEmittedTime = 0L
    collect {
        val itemReceivedTime = System.now().toEpochMilliseconds()
        val delta = itemReceivedTime - lastEmittedTime
        if (delta >= skipDuration) {
            emit(it)
            lastEmittedTime = System.now().toEpochMilliseconds()
        }
    }
}

/**
 * Returns MutableSharedFlow with PublishSubject like behaviour.
 */
fun <T> mutableSharedFlow() = MutableSharedFlow<T>(extraBufferCapacity = 1)

/**
 * Returns MutableSharedFlow with BehaviorSubject like behaviour, but without default value.
 */
fun <T> mutableSharedFlowWithLatest() = MutableSharedFlow<T>(replay = 1, extraBufferCapacity = 1)

/**
 * Caches and shares the last emitted value from a Flow ONLY when one or more downstream collectors are connected.
 *
 * * [stopTimeoutMillis] &mdash; Configures a delay (in milliseconds) between the disappearance of the last
 *   subscriber and the stopping of the sharing coroutine. It defaults to [STOP_TIMEOUT_MILLIS].
 * * [replayExpirationMillis] &mdash; Configures a delay (in milliseconds) between the stopping of
 *   the sharing coroutine and the resetting of the replay cache.
 *   It defaults to zero (expire the cache immediately after stopping of the sharing coroutine).
 *
 * Upstream sources will be shut down when no one is listening after [stopTimeoutMillis].
 * When there are no active subscribers, last emitted value will be cleared after
 * [stopTimeoutMillis] + [replayExpirationMillis].
 *
 * Use case: you want to cache emitted value during config change and/or between different screens.
 * Designed for long running expensive streams.
 *
 */
fun <T> Flow<T>.shareAndReplayLatest(
    stopTimeoutMillis: Long = STOP_TIMEOUT_MILLIS,
    replayExpirationMillis: Long = 0,
) = shareIn(
    scope = CoroutineScope(
        Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Timber.e("ShareAndReplayLatest failed: ${throwable.message}")
        }
    ),
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis, replayExpirationMillis),
    replay = 1,
)

/**
 * Sharing is started when the first subscriber appears and never stops.
 * Last emitted value will remain cached even after everyone stopped listening for it.
 * Similar to MutableSharedFlow(replay = 1, extraBufferCapacity = 1).
 * Use case: you want to get some data which is unlikely to change,
 * but don't want to send REST call or do a big query every time you need it.
 * For example, fetching userId which you need to use in other requests or
 * using LocalizationManager to find some string which is used on multiple places across the screen.
 */
fun <T> Flow<T>.shareAndReplayLatestForever() = shareIn(
    scope = CoroutineScope(
        Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Timber.e("ShareAndReplayLatestForever failed: ${throwable.message}")
        }
    ),
    started = SharingStarted.Lazily,
    replay = 1,
)
