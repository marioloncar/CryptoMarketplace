package com.marioloncar.data.tickers.data

import com.marioloncar.data.tickers.data.source.remote.TickersRemoteSource
import com.marioloncar.data.tickers.domain.TickersRepository
import kotlinx.coroutines.flow.Flow

internal class TickersRepositoryImpl(
    tickersRemoteSource: TickersRemoteSource,
) : TickersRepository {

    override fun getTickers(): Flow<List<String>> {
        TODO("Not yet implemented")
    }
}
