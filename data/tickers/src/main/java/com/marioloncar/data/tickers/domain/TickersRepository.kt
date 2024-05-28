package com.marioloncar.data.tickers.domain

import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for fetching ticker data.
 */
interface TickersRepository {

    /**
     * Retrieves a flow of ticker data.
     *
     * @param symbols A string representing the ticker symbols to fetch data for.
     * @return A [Flow] emitting lists of [Ticker] objects.
     */
    fun getTickers(symbols: String? = null): Flow<List<Ticker>>
}
