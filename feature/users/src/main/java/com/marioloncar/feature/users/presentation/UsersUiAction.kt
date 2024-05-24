package com.marioloncar.feature.users.presentation

/**
 * Possible UI actions on Users screen.
 */
sealed interface UsersUiAction {

    /**
     * Refresh users action.
     */
    data object RefreshClick : UsersUiAction
}
