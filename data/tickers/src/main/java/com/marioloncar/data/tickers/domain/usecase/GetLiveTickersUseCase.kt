package com.marioloncar.data.tickers.domain.usecase

import com.marioloncar.core.util.ticker.TickerUtil
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge

class GetLiveTickersUseCase(
    private val getTickersUseCase: GetTickersUseCase,
    private val tickerUtil: TickerUtil,
) {
    operator fun invoke(): Flow<List<Ticker>> {
        return merge(
            getTickersUseCase(),
            tickerUtil.createTicker(period = 5000)
                .flatMapLatest { getTickersUseCase() }
        )
    }
}
