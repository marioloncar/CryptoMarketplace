package com.marioloncar.data.tickers.data

import com.marioloncar.data.tickers.data.mapper.TickersRepositoryMapper
import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class TickersRepositoryImpl(
    private val tickersRemoteSource: TickersRemoteSource,
    private val tickersRepositoryMapper: TickersRepositoryMapper,
) : TickersRepository {

    override fun getTickers(symbols: String?): Flow<List<Ticker>> = flow {
        val tickers = tickersRemoteSource.fetchTickers(
            symbols = tickersRepositoryMapper.sanitizeSymbols(symbols)
        )

        val tickersList = tickersRepositoryMapper.toTickers(tickersJson = tickers)

        emit(tickersList)
    }
}
