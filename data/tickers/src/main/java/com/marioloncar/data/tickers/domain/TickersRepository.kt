package com.marioloncar.data.tickers.domain

import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow

interface TickersRepository {

    fun getTickers(symbols: String? = null): Flow<List<Ticker>>
}
