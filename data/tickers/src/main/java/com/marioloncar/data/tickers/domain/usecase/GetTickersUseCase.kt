package com.marioloncar.data.tickers.domain.usecase

import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow

class GetTickersUseCase(
    private val tickersRepository: TickersRepository,
) {

    operator fun invoke(): Flow<List<Ticker>> = tickersRepository.getTickers()
}
