package com.marioloncar.core.util.ticker

import kotlinx.coroutines.flow.Flow

interface TickerUtil {

    fun createTicker(period: Long): Flow<Unit>
}
