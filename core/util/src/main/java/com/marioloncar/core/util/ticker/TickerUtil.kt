package com.marioloncar.core.util.ticker

import kotlinx.coroutines.flow.Flow

/**
 * Utility interface for creating ticker updates at specified intervals.
 */
interface TickerUtil {

    /**
     * Creates ticker updates at the specified period.
     *
     * @param period The time period in milliseconds between ticker updates.
     * @return A [Flow] emitting [Unit] at the specified interval.
     */
    fun createTicker(period: Long): Flow<Unit>
}
