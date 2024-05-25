package com.marioloncar.feature.market.presentation

import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.data.tickers.domain.usecase.GetLiveTickersUseCase
import com.marioloncar.data.tickers.domain.usecase.GetTickersUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MarketViewModel(
    private val getLiveTickersUseCase: GetLiveTickersUseCase,
) : BaseViewModel<MarketUiState, MarketUiAction>() {

    private val toolbarUiState = observeUiState("") {
        flowOf("Market")
    }

    private val tickersUiState: StateFlow<MarketUiState.Tickers> = observeUiState(
        initialUiState = MarketUiState.Tickers.Loading
    ) {
        getLiveTickersUseCase()
            .map<List<Ticker>, MarketUiState.Tickers> { MarketUiState.Tickers.Content(it.map { it.pair }) }
            .onEach { println("testis $it") }
            .catch {
                emit(MarketUiState.Tickers.Error("Unknown error occurred."))
            }
    }

    override val uiState: StateFlow<MarketUiState> =
        observeUiState(initialUiState = MarketUiState()) {
            combine(toolbarUiState, tickersUiState) { title, tickers ->
                MarketUiState(title = title, tickers = tickers)
            }
        }

    override fun onActionInvoked(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OnSearchInput -> handleSearchInput(uiAction.searchTerm)
        }
    }

    private fun handleSearchInput(searchTerm: String) {
        // TODO Implement search.
    }

}
