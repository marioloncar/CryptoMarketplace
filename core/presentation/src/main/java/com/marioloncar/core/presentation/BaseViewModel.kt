package com.marioloncar.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import timber.log.Timber

/**
 * Provides utilities for managing UI state, executing asynchronous actions,
 * and observing flows.
 */
abstract class BaseViewModel : ViewModel() {

    private val className = "${this::class.simpleName} (${this.hashCode()})"

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("Error executing coroutine in $className", throwable)
    }

    protected val backgroundScope = viewModelScope + Dispatchers.IO + coroutineExceptionHandler

    init {
        Timber.d("Init: $className")
    }

    protected fun <T> Flow<T>.stateInViewModel(
        initialValue: T,
        startStrategy: SharingStarted = WhileSubscribed(SHARE_STOP_TIMEOUT_MILLIS),
    ) = stateIn(
        scope = backgroundScope,
        started = startStrategy,
        initialValue = initialValue,
    )

    protected fun <T> Flow<T>.shareInViewModel(
        replayExpirationMillis: Long = 0,
        startStrategy: SharingStarted = WhileSubscribed(
            SHARE_STOP_TIMEOUT_MILLIS,
            replayExpirationMillis
        ),
    ) = shareIn(
        scope = backgroundScope,
        started = startStrategy,
        replay = 1,
    )

    companion object {
        const val SHARE_STOP_TIMEOUT_MILLIS = 5_000L
    }
}
