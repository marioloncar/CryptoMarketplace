package com.marioloncar.data.tickers.domain

import kotlinx.coroutines.flow.Flow

interface TickersRepository {

    fun getTickers(): Flow<List<String>>
}
