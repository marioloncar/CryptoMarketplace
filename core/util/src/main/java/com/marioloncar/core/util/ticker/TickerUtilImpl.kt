package com.marioloncar.core.util.ticker

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [TickerUtil] responsible for creating ticker updates at specified intervals.
 */
class TickerUtilImpl : TickerUtil {

    /**
     * Creates ticker updates at the specified period.
     *
     * @param period The time period in milliseconds between ticker updates.
     * @return A [Flow] emitting [Unit] at the specified interval.
     */
    override fun createTicker(period: Long): Flow<Unit> = flow {
        while (true) {
            delay(period)
            emit(Unit)
        }
    }
}
