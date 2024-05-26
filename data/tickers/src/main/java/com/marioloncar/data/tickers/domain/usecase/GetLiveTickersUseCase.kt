package com.marioloncar.data.tickers.domain.usecase

import com.marioloncar.core.util.extension.shareAndReplayLatest
import com.marioloncar.core.util.ticker.TickerUtil
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge

class GetLiveTickersUseCase(
    private val getTickersUseCase: GetTickersUseCase,
    tickerUtil: TickerUtil,
) {

    private val liveTicker = merge(
        getTickersUseCase(),
        tickerUtil.createTicker(period = 5000).flatMapLatest { getTickersUseCase() }
    )
        .shareAndReplayLatest()

    operator fun invoke(): Flow<List<Ticker>> = liveTicker
}
