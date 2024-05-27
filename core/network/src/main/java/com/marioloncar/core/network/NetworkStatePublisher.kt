package com.marioloncar.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface NetworkStatePublisher {
    fun publishHasNetworkConnection(hasNetworkConnection: Boolean)
    fun hasNetworkConnection(): Flow<Boolean>
}

class NetworkStatePublisherImpl : NetworkStatePublisher {

    private val hasNetworkConnectionPublisher = MutableStateFlow(false)

    override fun publishHasNetworkConnection(hasNetworkConnection: Boolean) {
        hasNetworkConnectionPublisher.value = hasNetworkConnection
    }

    override fun hasNetworkConnection(): Flow<Boolean> = hasNetworkConnectionPublisher
}
