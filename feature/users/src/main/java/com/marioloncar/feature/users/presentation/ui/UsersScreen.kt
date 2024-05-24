@file:OptIn(ExperimentalMaterial3Api::class)

package com.marioloncar.feature.users.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marioloncar.core.ui.theme.CryptoMarketplaceTheme
import com.marioloncar.feature.users.presentation.UsersUiAction
import com.marioloncar.feature.users.presentation.UsersUiState
import com.marioloncar.feature.users.presentation.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UsersScreen(usersViewModel: UsersViewModel = koinViewModel(), modifier: Modifier = Modifier) {

    val uiState by usersViewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Toolbar(
                    uiState.title,
                    onRefreshClick = { usersViewModel.onActionInvoked(UsersUiAction.RefreshClick) },
                )
            },
            content = { innerPadding ->
                ContentList(uiState.users, modifier = Modifier.padding(innerPadding))
            }
        )
    }
}

@Composable
fun Toolbar(title: String, onRefreshClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        actions = {
            IconButton(onClick = { onRefreshClick() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        }
    )
}

@Composable
fun ContentList(usersUiState: UsersUiState.Users, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        when (usersUiState) {
            is UsersUiState.Users.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(usersUiState.users) {
                        Text(text = it)
                    }
                }
            }

            is UsersUiState.Users.Error -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: ${usersUiState.message}")
                }
            }

            UsersUiState.Users.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun RefreshButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Refresh")
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    CryptoMarketplaceTheme {
        UsersScreen()
    }
}
