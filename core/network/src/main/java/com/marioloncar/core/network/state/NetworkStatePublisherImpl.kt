package com.marioloncar.core.network.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Implementation of [NetworkStatePublisher] responsible for publishing and retrieving network connection state.
 */
class NetworkStatePublisherImpl : NetworkStatePublisher {

    private val hasNetworkConnectionPublisher = MutableStateFlow(false)

    /**
     * Publishes the current network connection state.
     *
     * @param hasNetworkConnection Boolean indicating whether there is a network connection.
     */
    override fun publishHasNetworkConnection(hasNetworkConnection: Boolean) {
        hasNetworkConnectionPublisher.value = hasNetworkConnection
    }

    /**
     * Retrieves the flow representing the network connection state.
     *
     * @return A [Flow] emitting Boolean values representing the network connection state.
     */
    override fun hasNetworkConnection(): Flow<Boolean> = hasNetworkConnectionPublisher
}
