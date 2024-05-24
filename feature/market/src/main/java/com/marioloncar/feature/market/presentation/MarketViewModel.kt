package com.marioloncar.feature.market.presentation

import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.data.tickers.domain.usecase.GetTickersUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MarketViewModel(
    private val getTickersUseCase: GetTickersUseCase,
) : BaseViewModel<MarketUiState, MarketUiAction>() {

    private val toolbarUiState = observeUiState("") {
        flowOf("Market")
    }

    private val tickersUiState: StateFlow<MarketUiState.Tickers> = observeUiState(
        initialUiState = MarketUiState.Tickers.Loading
    ) {
        getTickersUseCase()
            .map<List<Ticker>, MarketUiState.Tickers>  { MarketUiState.Tickers.Content(it.map { it.pair }) }
            .catch { emit(MarketUiState.Tickers.Error("Unknown error occurred.")) }
    }

    override fun onActionInvoked(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OnSearchInput -> handleSearchInput(uiAction.searchTerm)
        }
    }

    private fun handleSearchInput(searchTerm: String) {
        // TODO Implement search.
    }

    override val uiState: StateFlow<MarketUiState> = observeUiState(initialUiState = MarketUiState()) {
        combine(toolbarUiState, tickersUiState) { title, tickers ->
            MarketUiState(title = title, tickers = tickers)
        }
    }

}
