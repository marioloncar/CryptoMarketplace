package com.marioloncar.data.tickers.domain.usecase

import com.marioloncar.data.tickers.domain.TickersRepository
import com.marioloncar.data.tickers.domain.model.Ticker
import kotlinx.coroutines.flow.Flow

/**
 * Use case to retrieve tickers from the repository.
 *
 * @constructor tickersRepository The repository to fetch tickers from.
 */
class GetTickersUseCase(
    private val tickersRepository: TickersRepository,
) {

    /**
     * Invokes the use case to retrieve tickers.
     *
     * @param symbols A string representing the ticker symbols to fetch data for.
     *                If null, retrieves data for all available symbols.
     * @return A [Flow] emitting lists of [Ticker] objects.
     */
    operator fun invoke(symbols: String? = null): Flow<List<Ticker>> {
        return tickersRepository.getTickers(symbols)
    }
}
