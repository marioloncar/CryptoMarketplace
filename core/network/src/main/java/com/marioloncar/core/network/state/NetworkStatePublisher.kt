package com.marioloncar.core.network.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Interface for publishing and retrieving network connection state.
 */
interface NetworkStatePublisher {

    /**
     * Publishes the current network connection state.
     *
     * @param hasNetworkConnection Boolean indicating whether there is a network connection.
     */
    fun publishHasNetworkConnection(hasNetworkConnection: Boolean)

    /**
     * Retrieves the flow representing the network connection state.
     *
     * @return A [Flow] emitting Boolean values representing the network connection state.
     */
    fun hasNetworkConnection(): Flow<Boolean>
}
