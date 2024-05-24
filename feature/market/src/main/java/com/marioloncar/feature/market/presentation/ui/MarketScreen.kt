@file:OptIn(ExperimentalMaterial3Api::class)

package com.marioloncar.feature.market.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.marioloncar.feature.market.presentation.MarketUiAction
import com.marioloncar.feature.market.presentation.MarketUiState
import com.marioloncar.feature.market.presentation.MarketViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarketScreen(marketViewModel: MarketViewModel = koinViewModel(), modifier: Modifier = Modifier) {

    val uiState by marketViewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Toolbar(
                    uiState.title,
                    onSearchInput = { marketViewModel.onActionInvoked(MarketUiAction.OnSearchInput(it)) },
                )
            },
            content = { innerPadding ->
                ContentList(uiState.tickers, modifier = Modifier.padding(innerPadding))
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun Toolbar(title: String, onSearchInput: (String) -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        actions = {
            IconButton(onClick = { onSearchInput("") }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun ContentList(tickersUiState: MarketUiState.Tickers, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        when (tickersUiState) {
            is MarketUiState.Tickers.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(tickersUiState.tickers) {
                        Text(text = it)
                    }
                }
            }

            is MarketUiState.Tickers.Error -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error: ${tickersUiState.message}")
                }
            }

            MarketUiState.Tickers.Loading -> {
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

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    CryptoMarketplaceTheme {
        MarketScreen()
    }
}
