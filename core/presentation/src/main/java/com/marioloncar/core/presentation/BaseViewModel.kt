package com.marioloncar.core.presentation

import androidx.lifecycle.ViewModel
import com.marioloncar.core.util.safeCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

private const val SHARING_STOP_TIMEOUT_MILLIS = 5000L

/**
 * This class provides utilities for managing UI state, executing asynchronous actions,
 * and observing flows.
 *
 * @param ScreenUiState The type representing the UI state.
 * @param ScreenUiAction The type representing the UI actions.
 */
abstract class BaseViewModel<ScreenUiState : Any, ScreenUiAction : Any> : ViewModel() {

    private val className = "${this::class.simpleName} (${this.hashCode()})"

    private val job = SupervisorJob()

    protected val backgroundScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    /**
     * Representing the current UI state as a StateFlow.
     */
    abstract val uiState: StateFlow<ScreenUiState>

    /**
     * Abstract method to handle UI actions.
     *
     * @param uiAction The UI action to be handled.
     */
    abstract fun onActionInvoked(uiAction: ScreenUiAction)

    init {
        Timber.d("Init: $className")
    }

    /**
     * Executes a suspend function in the background scope.
     * Used for one shot operations or mutating data source.
     *
     * @param action The suspend function to be executed.
     */
    protected fun doInBackground(action: suspend () -> Unit) {
        backgroundScope.launch(safeCoroutineExceptionHandler { _, throwable ->
            Timber.e(
                "Error executing: ${action::class} in $className",
                throwable
            )
        }) {
            action()
        }
    }

    /**
     * Observes a Flow of UI state with error handling and logging.
     * Used for streams of data i.e. listening to data source.
     *
     * initialUiState - The initial state of the UI represented by this ViewModel.
     * flowFactory - A function to create the Flow of UI state.
     *
     * returns StateFlow representing the observed UI state.
     */
    protected fun <UiState> observeUiState(
        initialUiState: UiState,
        flowFactory: () -> Flow<UiState>,
    ): StateFlow<UiState> {
        return flowFactory()
            .catch { throwable ->
                Timber.e(
                    "Error observing: $className - ${flowFactory::class}",
                    throwable
                )
            }
            .stateIn(
                scope = backgroundScope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = SHARING_STOP_TIMEOUT_MILLIS
                ),
                initialValue = initialUiState,
            )
    }

    /**
     * Permanently cancels the coroutine job.
     */
    override fun onCleared() {
        Timber.d("$className destroyed.")
        job.cancel()
    }
}
