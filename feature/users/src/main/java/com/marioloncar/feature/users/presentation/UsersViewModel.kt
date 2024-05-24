package com.marioloncar.feature.users.presentation

import com.marioloncar.core.presentation.BaseViewModel
import com.marioloncar.data.users.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class UsersViewModel(
    private val getUsersUseCase: GetUsersUseCase,
) : BaseViewModel<UsersUiState, UsersUiAction>() {

    private val refreshUsers = MutableSharedFlow<Unit>()

    private val usersUiState: StateFlow<UsersUiState.Users> =
        observeUiState(initialUiState = UsersUiState.Users.Loading) {
            refreshUsers
                .onStart { emit(Unit) }
                .flatMapLatest { getUsersUseCase() }
                .map<List<String>, UsersUiState.Users> { UsersUiState.Users.Content(it) }
                .catch { emit(UsersUiState.Users.Error("Unknown error occurred.")) }
        }

    private val toolbarUiState = observeUiState("") {
        flowOf("Users")
    }

    override val uiState: StateFlow<UsersUiState> =
        observeUiState(initialUiState = UsersUiState()) {
            combine(toolbarUiState, usersUiState) { title, users ->
                UsersUiState(title = title, users = users)
            }
        }

    override fun onActionInvoked(uiAction: UsersUiAction) {
        when (uiAction) {
            UsersUiAction.RefreshClick -> handleRefreshClick()
        }
    }

    private fun handleRefreshClick() {
        doInBackground { refreshUsers.emit(Unit) }
    }
}
