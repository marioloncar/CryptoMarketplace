package com.marioloncar.feature.market.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.marioloncar.core.network.NetworkStatePublisher
import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.data.tickers.domain.usecase.GetLiveTickersUseCase
import com.marioloncar.feature.market.R
import com.marioloncar.feature.market.presentation.mapper.MarketUiStateMapper
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MarketViewModel(
    private val getLiveTickersUseCase: GetLiveTickersUseCase,
    private val marketUiStateMapper: MarketUiStateMapper,
    private val networkStatePublisher: NetworkStatePublisher,
) : BaseViewModel<MarketUiState, MarketUiAction>() {

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 500L
    }

    var searchQuery by mutableStateOf("")
        private set

    private val toolbarUiState: StateFlow<Int> = observeUiState(R.string.empty) {
        flowOf(marketUiStateMapper.toTitle())
    }

    private val tickersUiState: StateFlow<MarketUiState.Tickers> = observeUiState(
        initialUiState = MarketUiState.Tickers.Loading
    ) {
        networkStatePublisher.hasNetworkConnection()
            .distinctUntilChanged()
            .flatMapLatest { hasNetworkConnection ->
                if (hasNetworkConnection) {
                    snapshotFlow { searchQuery }
                        .distinctUntilChanged()
                        .debounce(SEARCH_DEBOUNCE_MILLIS)
                        .flatMapLatest {
                            getLiveTickersUseCase(it)
                                .map<List<Ticker>, MarketUiState.Tickers>(marketUiStateMapper::toContent)
                                .catch { emit(marketUiStateMapper.toError()) }
                        }
                } else {
                    flowOf(marketUiStateMapper.toNetworkConnectionError())
                }
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
            is MarketUiAction.OnSearchInput -> handleSearchInput(searchTerm = uiAction.searchQuery)
        }
    }

    private fun handleSearchInput(searchTerm: String) {
        searchQuery = searchTerm
    }
}
