package com.marioloncar.feature.market.presentation

/**
 * Possible UI actions on Market screen.
 */
sealed interface MarketUiAction {

    /**
     * Represents an action triggered by a search input.
     *
     * @property searchQuery The search query entered by the user.
     */
    data class OnSearchInput(val searchQuery: String) : MarketUiAction
}
