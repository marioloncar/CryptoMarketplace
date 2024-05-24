package com.marioloncar.feature.users.presentation

/**
 * UI state definitions for Users screen.
 */
data class UsersUiState(
    val title: String = "",
    val users: Users = Users.Loading,
) {

    /**
     * Mutually exclusive states for users.
     */
    sealed interface Users {
        data class Content(val users: List<String>) : Users
        data class Error(val message: String) : Users
        data object Loading : Users
    }
}
