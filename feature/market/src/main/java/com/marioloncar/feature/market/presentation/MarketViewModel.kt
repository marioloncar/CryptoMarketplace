package com.marioloncar.feature.market.presentation

import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.tickers.domain.model.Ticker
import com.marioloncar.data.tickers.domain.usecase.GetLiveTickersUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

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
            .map<List<Ticker>, MarketUiState.Tickers> { tickers ->
                MarketUiState.Tickers.Content(
                    tickers.map { ticker ->
                        MarketUiState.TickerData(
                            name = ticker.pair,
                            isEarnYield = ticker.dailyChange > 0,
                            bid = formatPrice(ticker.bid),
                            dailyChange = formatDailyChange(dailyChange = ticker.dailyChange)
                        )
                    }
                )
            }
            .catch {
                emit(MarketUiState.Tickers.Error("Unknown error occurred."))
            }
    }

    // TODO Extract to mapper.
    private fun formatDailyChange(dailyChange: Double): String {
        return String.format(Locale.US, "%.2f%%", dailyChange * 100, Locale.US)
    }

    // TODO Extract to mapper.
    private fun formatPrice(price: Double): String {
        return "$${BigDecimal(price).setScale(6, RoundingMode.HALF_UP).toPlainString()}"
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
