package com.marioloncar.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marioloncar.core.util.safeCoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

/**
 * Provides utilities for managing UI state, executing asynchronous actions,
 * and observing flows.
 */
abstract class BaseViewModel : ViewModel() {

    private val className = "${this::class.simpleName} (${this.hashCode()})"

    protected val backgroundScope = viewModelScope + Dispatchers.IO

    init {
        Timber.d("Init: $className")
    }

    /**
     * Executes a suspend function in the background scope.
     * Used for one shot operations or mutating data source.
     *
     * @param action The suspend function to be executed.
     */
    protected fun launchInBackground(action: suspend () -> Unit) {
        backgroundScope.launch(
            safeCoroutineExceptionHandler { _, throwable ->
                Timber.e(
                    "Error executing: ${action::class} in $className",
                    throwable
                )
            }) { action() }
    }

    /**
     * Helper extension for [stateIn] operator.
     */
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
