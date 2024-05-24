package com.marioloncar.feature.market.presentation

/**
 * Possible UI actions on Market screen.
 */
sealed interface MarketUiAction {

    data class OnSearchInput(val searchTerm: String) : MarketUiAction
}
