package com.marioloncar.core.util.ticker

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TickerUtilImpl : TickerUtil {

    override fun createTicker(period: Long): Flow<Unit> = flow {
        while (true) {
            delay(period)
            emit(Unit)
        }
    }
}
