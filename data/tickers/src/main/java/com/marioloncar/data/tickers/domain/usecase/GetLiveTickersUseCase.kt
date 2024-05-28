package com.marioloncar.data.tickers.domain.usecase

import com.marioloncar.core.util.ticker.TickerUtil
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge

/**
 * Use case to get live ticker data.
 *
 * @constructor getTickersUseCase The use case to get tickers.
 * @constructor tickerUtil Utility for creating tickers.
 */
class GetLiveTickersUseCase(
    private val getTickersUseCase: GetTickersUseCase,
    private val tickerUtil: TickerUtil,
) {

    /**
     * Invokes the use case to get live ticker data.
     *
     * @param symbols A string representing the ticker symbols to fetch data for. Can be null to fetch all symbols.
     * @return A [Flow] emitting lists of [Ticker] objects, updated periodically.
     */
    operator fun invoke(symbols: String?): Flow<List<Ticker>> = merge(
        getTickersUseCase(symbols),
        tickerUtil.createTicker(period = 5000).flatMapLatest { getTickersUseCase(symbols) }
    )
}
