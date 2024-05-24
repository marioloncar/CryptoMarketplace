package com.marioloncar.feature.market.presentation

import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.usecase.GetTickersUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class MarketViewModel(
    private val getTickersUseCase: GetTickersUseCase,
) : BaseViewModel<MarketUiState, MarketUiAction>() {

    private val toolbarUiState = observeUiState("") {
        flowOf("Market")
    }

    override fun onActionInvoked(uiAction: MarketUiAction) {
        when (uiAction) {
            is MarketUiAction.OnSearchInput -> handleSearchInput(uiAction.searchTerm)
        }
    }

    private fun handleSearchInput(searchTerm: String) {
        TODO("Not yet implemented.")
    }

    override val uiState: StateFlow<MarketUiState>
        get() = TODO("Not yet implemented")
}
