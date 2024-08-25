package com.marioloncar.feature.market.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.marioloncar.core.network.state.NetworkStatePublisher
import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.data.tickers.domain.usecase.GetLiveTickersUseCase
import com.marioloncar.feature.market.presentation.mapper.MarketUiStateMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Presentation logic for Market screen.
 */
class MarketViewModel(
    private val getLiveTickersUseCase: GetLiveTickersUseCase,
    private val marketUiStateMapper: MarketUiStateMapper,
    networkStatePublisher: NetworkStatePublisher,
) : BaseViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    private val tickersUiState: Flow<MarketUiState.Tickers> =
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

    val uiState: StateFlow<MarketUiState> = tickersUiState
        .map { MarketUiState(tickers = it) }
        .stateInViewModel(MarketUiState())

    fun onSearchInput(searchTerm: String) {
        searchQuery = searchTerm
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 500L
    }
}
